package edu.gatech.chai.pacer.api;

import edu.gatech.chai.pacer.model.Organization;
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
public class ManageApiControllerIntegrationTest {

    @Autowired
    private ManageApi api;

    @Test
    public void addOrganizationTest() throws Exception {
        Organization body = new Organization();
        ResponseEntity<Void> responseEntity = api.addOrganization(body);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

    @Test
    public void deleteOrganizationTest() throws Exception {
        Integer id = 56;
        ResponseEntity<Void> responseEntity = api.deleteOrganization(id);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

    @Test
    public void getOrganizationTest() throws Exception {
        Integer id = 56;
        ResponseEntity<Organization> responseEntity = api.getOrganization(id);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

    @Test
    public void getOrganizationsTest() throws Exception {
        ResponseEntity<Organizations> responseEntity = api.getOrganizations();
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

    @Test
    public void updateOrganizationTest() throws Exception {
        Integer id = 56;
        Organization body = new Organization();
        ResponseEntity<Void> responseEntity = api.updateOrganization(id, body);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

}
