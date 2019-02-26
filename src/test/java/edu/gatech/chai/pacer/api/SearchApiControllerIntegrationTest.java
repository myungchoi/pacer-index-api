package edu.gatech.chai.pacer.api;

import edu.gatech.chai.pacer.model.Organizations;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchApiControllerIntegrationTest {

    @Autowired
    private SearchApi api;

    @Test
    public void searchDecedentTest() throws Exception {
        String organizationName = "organizationName_example";
        String organizationId = "organizationId_example";
        ResponseEntity<Organizations> responseEntity = api.searchDecedent(organizationName, organizationId);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

}
