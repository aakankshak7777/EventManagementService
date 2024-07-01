package com.kmbl.eventmanagementservice.service;

import static com.kmbl.eventmanagementservice.utils.ThreadUtil.newThreadFactory;

import com.kmbl.eventmanagementservice.dao.CollectCallbackDao;
import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.exceptions.CollectCallbackExistsException;
import com.kmbl.eventmanagementservice.service.dtos.CollectCallback;
import com.kmbl.eventmanagementservice.service.dtos.CollectCallbackEvent;
import com.kmbl.eventmanagementservice.service.dtos.requests.CollectCallbackRequest;
import com.kmbl.eventmanagementservice.service.event.CollectorCallbackEventService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.concurrent.ThreadSafe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@ThreadSafe
@Service
public class CollectCallbackService {
    private final CollectCallbackDao dao;
    private final CollectorCallbackEventService collectorCallbackEventService;
    private final ExecutorService workerExecutor;

//    @Value("${ems-collect-callback-service-max-thread}")
    private int max_Thread = 5;

    public CollectCallbackService(CollectCallbackDao dao, CollectorCallbackEventService collectorCallbackEventService) {
        this.dao = dao;
        this.collectorCallbackEventService = collectorCallbackEventService;
        this.workerExecutor = Executors.newFixedThreadPool(this.max_Thread, newThreadFactory("ems-collect-callback-publisher"));;

    }

    public void processCallbackEvent(CollectCallbackRequest request) {
        var collectCallback = request.toCollectCallbackRequest(EventName.COLLECT_CALLBACK_API, EventStatus.PENDING);
        var collectCallbackEvent = request.toCollectCallbackEvent(EventName.COLLECT_CALLBACK_API);
        if (!recordCallbackEvent(collectCallback)) {
            var isRecordPresent = dao.getByTransactionIdAndType(collectCallback.transactionId(), collectCallback.type()).
                    isPresent();
            if (isRecordPresent)
                throw new CollectCallbackExistsException("Event is already created for event: " + collectCallback);

            throw new CollectCallbackExistsException("There is problem creating event: " + collectCallback);
        }
        workerExecutor.submit(() -> {
            if (!publishCallbackEvent(collectCallbackEvent))
            {
                updateCallbackEvent(collectCallbackEvent, EventStatus.FAIL);
            }
        });

        log.info("Event is processed " + collectCallbackEvent);
    }

    public boolean updateCallbackEvent(CollectCallbackEvent collectCallbackEvent, EventStatus eventStatus) {
        var collectCallback = collectCallbackEvent.toCollectCallback(eventStatus);
        try  {
            dao.update(collectCallback);
        } catch (Exception e) {
            log.info("Failed to update record {}: ",  collectCallback);
            return false;
        }
        return true;
    }

    private boolean recordCallbackEvent(CollectCallback collectCallback) {
        try  {
            dao.create(collectCallback);
        } catch (CollectCallbackExistsException e) {
            return false;
        } catch(Exception ex)
        {
            log.error("Failed while creating Event record {}" , collectCallback );
            return false;
        }

        return true;
    }
    private boolean publishCallbackEvent(CollectCallbackEvent collectCallbackEvent) {
        try {
            collectorCallbackEventService.queueUp(collectCallbackEvent);
            return true;
           } catch (Exception e) {
             log.info("New event is not published with transactionId" + collectCallbackEvent.transactionId() + " and type "
                + collectCallbackEvent.type());
             return false;
        }
    }
}
