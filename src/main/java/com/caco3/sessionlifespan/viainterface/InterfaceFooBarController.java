package com.caco3.sessionlifespan.viainterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;

@Controller
@RequestMapping("/interface")
@SessionAttributes(InterfaceFooBarController.FOO_BAR_ATTRIBUTE)
@ResponseStatus(HttpStatus.OK)
public class InterfaceFooBarController {
    public static final String FOO_BAR_ATTRIBUTE = "fooBar";

    private static final Logger logger = LoggerFactory.getLogger(InterfaceFooBarController.class);

    @PostMapping
    public void putToSession(
            // Model model,
            FooBarImpl fooBar) {
        logger.info("fooBar = {} put into session implicitly", fooBar);
//		model.addAttribute(FOO_BAR_ATTRIBUTE, fooBar);
    }

    @GetMapping
    public void getFoobar(@ModelAttribute(FOO_BAR_ATTRIBUTE) FooBar fooBar, OutputStream outputStream) throws Exception {
        logger.info("got fooBar = {}", fooBar);
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream)) {
            outputStreamWriter.write(fooBar.getBaz());
        }
    }

    @GetMapping("/model")
    public void getModel(Model model) {
        model.asMap().forEach((name, value) -> logger.info("Model: '{}' -> '{}'", name, value));
    }

    @GetMapping("/session")
    public void getSession(HttpSession httpSession) {
        Enumeration<String> names = httpSession.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            logger.info("Session: '{}' -> '{}'", name, httpSession.getAttribute(name));
        }
    }
}
