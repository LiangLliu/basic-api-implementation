package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RsEvent {

    public interface PublicView {
    }

    public interface PrivateView extends PublicView {
    }


    @JsonView(PublicView.class)
    private String eventName;

    @JsonView(PublicView.class)
    private String keyWord;

    @JsonView(PrivateView.class)
    private @Valid User user;


}
