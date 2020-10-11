package com.caco3.sessionlifespan.viainterface;

import com.caco3.sessionlifespan.viaclazz.ClassFooBarControllerTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = InterfaceFooBarController.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InterfaceFooBarControllerTest {
    private static final String WRONG_FOO_BAR_ATTRIBUTE_NAME = "fooBar";
    private static final String BAZ = "myBaz";

    private final MockMvc mockMvc;
    private final MockHttpSession session = new MockHttpSession();

    public InterfaceFooBarControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    /**
     * This hits {@link InterfaceFooBarController#putToSession(FooBarImpl)}
     * <p>
     * The {@link FooBarImpl} will have attribute name equal to "fooBarImpl" as
     * explained in the {@link ClassFooBarControllerTest#putFooBarToSession()}.
     * <p>
     * The name is different from the expected attribute name, so the test fails.
     * <p>
     * TODO: How can we specify a name for such an attribute explicitly?
     * TODO: Using {@code @ModelAttribute("fooBar")} results in an exception
     * TODO: One of the possibilities is given in the comments of
     * TODO: {@link InterfaceFooBarController#putToSession(FooBarImpl)}
     */
    @Order(1)
    @Test
    void putFooBarToSession() throws Exception {
        mockMvc.perform(
                post("/interface")
                        .param("baz", BAZ)
                        .session(session)
        );

        assertEquals(new FooBarImpl(BAZ), session.getAttribute(WRONG_FOO_BAR_ATTRIBUTE_NAME));
    }

    /**
     * Hits {@link InterfaceFooBarController#getFoobar(FooBar, OutputStream)}
     * <p>
     * Fails with "Expected session attribute 'fooBar'" exception, because the name
     * of the attribute is different. Try to change the {@link InterfaceFooBarController#FOO_BAR_ATTRIBUTE}
     * to "fooBarImpl" and this test will pass
     */
    @Order(2)
    @Test
    void getFooBarFromSession() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/interface")
                        .session(session)
        ).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        assertEquals(BAZ, contentAsString);
    }
}