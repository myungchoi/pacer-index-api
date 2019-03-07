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

import edu.gatech.chai.pacer.dao.PacerDaoImpl;
import edu.gatech.chai.pacer.model.Organizations;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.threeten.bp.OffsetDateTime;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-02-26T15:28:40.714830-05:00[America/New_York]")
@Controller
public class SearchApiController implements SearchApi {

    private static final Logger log = LoggerFactory.getLogger(SearchApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

	@org.springframework.beans.factory.annotation.Autowired
	private PacerDaoImpl pacerDao;

    @org.springframework.beans.factory.annotation.Autowired
    public SearchApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Organizations> searchOrganization(@ApiParam(value = "Name of Provider") @Valid @RequestParam(value = "provider-name", required = false) String providerName,@ApiParam(value = "Organization ID Set (Type:Id)") @Valid @RequestParam(value = "organization-id", required = false) String organizationId) {
        String accept = request.getHeader("Accept");
        
        Organizations organizations = null;
        
        if (providerName != null && organizationId != null) {
        	organizations = pacerDao.getByProviderNameAndIdentifier(providerName, organizationId);
        } else if (providerName != null) {
        	organizations = pacerDao.getByProviderName(providerName);
        } else if (organizationId != null) {
        	organizations = pacerDao.getByIdentifier(organizationId);
        } else {
        	// both are null. Since both are optional. We return empty organizations.
        	organizations = new Organizations();
        	organizations.setCount(0);
        	organizations.setCreated(OffsetDateTime.now());
        }
        
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<Organizations>(organizations, headers, HttpStatus.OK);
    }

}
