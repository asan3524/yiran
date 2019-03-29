package com.yiran.base.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ApplicationTest {

    protected static final Logger logger = LoggerFactory.getLogger(ApplicationTest.class);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        logger.info("===setUpBeforeClass=====");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        logger.info("===tearDownAfterClass===");
    }

    @Before
    public void setUp() throws Exception {
        logger.info("=========setUp==========");
    }

    @After
    public void tearDown() throws Exception {
        logger.info("=========tearDown=======");
    }

    @Test
    public void testAssertEquals() {
        logger.debug("testAssertEquals");
        assertEquals("a", "a");
    }

    @Test
    public void testAssertTrue() {
        logger.debug("testAssertTrue");
        assertTrue(true);
    }

    @Test
    public void testAssertFalse() {
        logger.debug("testAssertFalse");
        assertFalse(false);
    }

    @Test(expected = RuntimeException.class)
    public void testException() {
        logger.debug("testException");
        throw new RuntimeException();
    }
}