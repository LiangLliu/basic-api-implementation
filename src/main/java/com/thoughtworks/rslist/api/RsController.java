package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/rs")
public class RsController {
    private List<String> rsList = Arrays.asList("第一条事件", "第二条事件", "第三条事件");

    @GetMapping("/list")
    public String getAllRsEvent() {
        return rsList.toString();
    }

    @GetMapping("/{index}")
    public String getOneRsEvent(@PathVariable Integer index) {
        return rsList.get(index - 1);
    }
}
