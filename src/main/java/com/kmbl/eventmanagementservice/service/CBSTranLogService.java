package com.kmbl.eventmanagementservice.service;

import com.kmbl.eventmanagementservice.dao.CBSTranLogDao;
import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.exceptions.CBSTranLogExistsException;
import com.kmbl.eventmanagementservice.model.CBSTranLogEvent;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLog;
import com.kmbl.eventmanagementservice.service.event.CBSTranLogGGEventService;
import com.kmbl.eventmanagementservice.service.requests.CBSTranLogRequest;

import javax.annotation.concurrent.ThreadSafe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.kmbl.eventmanagementservice.utils.ThreadUtils.newThreadFactory;

@Slf4j
@ThreadSafe
@Service
public class CBSTranLogService {
        private final CBSTranLogDao dao;
        private final CBSTranLogGGEventService cbsTranLogGGEventService;
        private final ExecutorService workerExecutor;
        //    @Value("${ems-collect-callback-service-max-thread}")
        private int max_Thread = 5;

        public CBSTranLogService(CBSTranLogDao dao, CBSTranLogGGEventService cbsTranLogGGEventService) {
            this.dao = dao;
            this.cbsTranLogGGEventService = cbsTranLogGGEventService;
            this.workerExecutor = Executors.newFixedThreadPool(this.max_Thread, newThreadFactory("ems-collect-callback-publisher"));;

        }

        public void processCallbackEvent(CBSTranLogRequest request) {
            var cbsTranLog = request.toCBSTranLog(EventName.COLLECT_CALLBACK_API, EventStatus.PENDING);
            var cbsTranLogEvent = request.CBSTranLogEvent(EventName.COLLECT_CALLBACK_API);
            if (!recordCBSTranLogEvent(cbsTranLog)) {
                var isRecordPresent = dao.getByTransactionIdAndType(cbsTranLog.transactionId(), cbsTranLog.type()).
                        isPresent();
                if (isRecordPresent)
                    throw new CBSTranLogExistsException("Event is already created for event: " + cbsTranLog);

                throw new CBSTranLogExistsException("There is problem creating event: " + cbsTranLog);
            }
            workerExecutor.submit(() -> {
                if (!publishCBSTranLogEvent(cbsTranLogEvent))
                {
                    updateCBSTranLogEvent(cbsTranLogEvent, EventStatus.FAIL);
                }
            });

            log.info("Event is processed " + cbsTranLogEvent);
        }

        public boolean updateCBSTranLogEvent(CBSTranLogEvent cbsTranLogEvent, EventStatus eventStatus) {
            var cbsTranLog = cbsTranLogEvent.toCBSTranLog(eventStatus);
            try  {
                dao.update(cbsTranLog);
            } catch (Exception e) {
                log.info("Failed to update record {}: ",  cbsTranLog);
                return false;
            }
            return true;
        }

        private boolean recordCBSTranLogEvent(CBSTranLog cbsTranLog) {
            try  {
                dao.create(cbsTranLog);
            } catch (CBSTranLogExistsException e) {
                return false;
            } catch(Exception ex)
            {
                log.error("Failed while creating Event record {}" , cbsTranLog);
                return false;
            }

            return true;
        }

        private boolean publishCBSTranLogEvent(CBSTranLogEvent cbsTranLogEvent) {
            try {
                cbsTranLogGGEventService.queueUp(cbsTranLogEvent);
                return true;
            } catch (Exception e) {
                log.info("New event is not published with transactionId" + cbsTranLogEvent.txnId() + " and type "
                        + cbsTranLogEvent.type());
                return false;
            }
        }
    }
