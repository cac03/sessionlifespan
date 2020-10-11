package com.caco3.sessionlifespan.viaclazz;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.method.annotation.ModelFactory;

import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ClassFooBarController.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClassFooBarControllerTest {
    private static final String BAZ = "myBaz";

    private final MockMvc mockMvc;
    private final MockHttpSession session = new MockHttpSession();

    public ClassFooBarControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    /**
     * This hits {@link ClassFooBarController#putToSession(FooBar)} with
     * a {@link FooBar} having baz = {@link ClassFooBarControllerTest#BAZ}.
     * <p>
     * The wired parameter will have {@link ModelAttribute} name equal
     * to "fooBar", this is done via {@link ModelFactory#getNameForParameter(MethodParameter)}
     * <p>
     * Since the parameter does not have {@link ModelAttribute} annotation
     * the {@link ModelFactory#getNameForParameter(MethodParameter)} falls back
     * to {@link Conventions#getVariableNameForParameter(MethodParameter)}, which
     * in turn uses parameter type class name (in this case "FooBar")
     */
    @Order(1)
    @Test
    public void putFooBarToSession() throws Exception {
        mockMvc.perform(
                post("/clazz")
                        .param("baz", BAZ)
                        .session(session)
        ).andReturn();

        assertEquals(new FooBar(BAZ), session.getAttribute("fooBar"));
    }

    /**
     * This hits {@link ClassFooBarController#getFoobar(FooBar, OutputStream)}
     * <p>
     * The endpoint will use previously stored session attribute to wire the first handler
     * method parameter, so we expect to see {@link ClassFooBarControllerTest#BAZ} in the
     * response body
     * <p>
     * Summarizing the two tests:
     * 1. In one session we hit two endpoints:
     * 1.1. {@link ClassFooBarController#putToSession(FooBar)}
     * 1.1.1. After the endpoint hit the session will have an attribute equal to posted FooBar with name = "fooBar"
     * 1.2. {@link ClassFooBarController#getFoobar(FooBar, OutputStream)}
     * 1.2.1. The first parameter is the FooBar put in the previous step (note that the attribute name is the same in all steps)
     * 2. This test is successful because:
     * 2.1. The {@link ClassFooBarController} is annotated with {@link SessionAttributes}
     * having the expected name
     * 2.2. The name of attribute is the same throughout all uses (though implicitly)
     */
    @Order(2)
    @Test
    void getFooBarFromSession() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/clazz")
                        .session(session)
        ).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        assertEquals(BAZ, contentAsString);
    }
}