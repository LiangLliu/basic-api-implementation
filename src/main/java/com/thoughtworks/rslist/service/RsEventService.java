package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.request.RsEventRequest;

public interface RsEventService {
    RsEventDto save(RsEventRequest rsEventRequest);

    long getRsListLength();

    RsEventDto findById(Integer id);

    RsEventDto updateRsEvent(RsEventDto rsEventDto);
}
