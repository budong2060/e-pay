package com.pay.test;

import com.pay.boot.PayApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by admin on 2017/9/6.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PayApplication.class})
@WebMvcTest
public abstract class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    static {
        System.setProperty("spring.active.profile", "dev");
    }

    @Before
    public void setup() {

    }

    /**
     * post with json
     * @param url
     * @param json
     * @return
     * @throws Exception
     */
    protected String postWithJson(String url, String json) throws Exception {
        String result = this.mockMvc.perform(post(url).accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        return result;
    }

    /**
     * put with json
     * @param url
     * @param json
     * @param uriVars
     * @return
     * @throws Exception
     */
    protected String putWithJson(String url, String json, Object... uriVars) throws Exception {
        String result = this.mockMvc.perform(put(url, uriVars).accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        return result;
    }

    /**
     * get by vars
     * @param url
     * @param uriVars
     * @return
     * @throws Exception
     */
    protected String getByVar(String url, Object... uriVars) throws Exception {
        String result = this.mockMvc.perform(get(url, uriVars))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        return result;
    }

    /**
     * delete by var
     * @param url
     * @param uriVars
     * @return
     * @throws Exception
     */
    protected String deleteByVar(String url, Object... uriVars) throws Exception {
        String result = this.mockMvc.perform(delete(url, uriVars))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        return result;
    }


}
