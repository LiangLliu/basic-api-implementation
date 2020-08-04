package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/rs")
public class RsController {

    private final List<RsEvent> rsList;

    {
        rsList = Stream.of(
                RsEvent.builder().eventName("第一条事件").keyWord("无分类").build(),
                RsEvent.builder().eventName("第二条事件").keyWord("无分类").build(),
                RsEvent.builder().eventName("第三条事件").keyWord("无分类").build()
        ).collect(Collectors.toList());
    }


    @GetMapping("/list")
    public List<RsEvent> getAllRsEvent(@RequestParam(required = false) Integer start,
                                       @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return rsList;
        }
        return rsList.subList(start - 1, end);
    }

    @GetMapping("/{index}")
    public RsEvent getOneRsEvent(@PathVariable Integer index) {
        return rsList.get(index - 1);
    }

    @PostMapping("/event")
    public void addOneRsEvent(@RequestBody RsEvent rsEvent) {
        rsList.add(rsEvent);
    }


}
