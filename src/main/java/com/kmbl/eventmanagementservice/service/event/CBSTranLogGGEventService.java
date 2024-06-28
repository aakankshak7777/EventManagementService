package com.kmbl.eventmanagementservice.service.event;

import com.kmbl.eventmanagementservice.model.CBSTranLogGGEvent;
import javax.annotation.concurrent.ThreadSafe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@ThreadSafe
@Service
public class CBSTranLogGGEventService {
    public void publishMessage(CBSTranLogGGEvent cbsTranLogGGEvent) {

    }
}
