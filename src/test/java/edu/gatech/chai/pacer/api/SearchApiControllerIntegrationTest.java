/*******************************************************************************
 * Copyright (c) 2019 Georgia Tech Research Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
        ResponseEntity<Organizations> responseEntity = api.searchOrganization(organizationName, organizationId);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

}
