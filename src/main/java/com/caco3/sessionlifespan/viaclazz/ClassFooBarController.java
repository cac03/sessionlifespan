package com.caco3.sessionlifespan.viaclazz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

@Controller
@RequestMapping("/clazz")
@SessionAttributes("fooBar")
@ResponseStatus(HttpStatus.OK)
public class ClassFooBarController {
    private static final Logger logger = LoggerFactory.getLogger(ClassFooBarController.class);

    @PostMapping
    public void putToSession(FooBar fooBar) {
        logger.info("fooBar = {} put into session implicitly", fooBar);
    }

    @GetMapping
    public void getFoobar(FooBar fooBar, OutputStream outputStream) throws Exception {
        logger.info("got fooBar = {}", fooBar);
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream)) {
            outputStreamWriter.write(fooBar.getBaz());
        }
    }
}
