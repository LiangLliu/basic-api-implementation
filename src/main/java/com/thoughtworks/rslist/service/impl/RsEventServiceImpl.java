package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;

import com.thoughtworks.rslist.request.RsEventRequest;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RsEventServiceImpl implements RsEventService {

    private final RsEventRepository rsEventRepository;

    public RsEventServiceImpl(RsEventRepository rsEventRepository) {
        this.rsEventRepository = rsEventRepository;
    }

    @Override
    public RsEventDto save(RsEventRequest rsEventRequest) {
        RsEventEntity rsEventEntity = rsEventRepository.save(RsEvent.toRsEntity(rsEventRequest));
        return RsEventDto.from(rsEventEntity);
    }

    @Override
    public long getRsListLength() {
        return rsEventRepository.count();
    }

    @Override
    public RsEventDto findById(Integer rsEventId) {
        RsEventEntity rsEventEntity = rsEventRepository.findById(rsEventId).get();
        return RsEventDto.from(rsEventEntity);
    }

    @Override
    public RsEventDto updateRsEvent(RsEventDto rsEventDto) {
        RsEventEntity saveEvent = rsEventRepository.save(RsEvent.toRsEntity(rsEventDto));
        return RsEventDto.from(saveEvent);
    }

    @Override
    public boolean isExistUserById(Integer rsEventId) {
        return rsEventRepository.existsById(rsEventId);
    }
}
