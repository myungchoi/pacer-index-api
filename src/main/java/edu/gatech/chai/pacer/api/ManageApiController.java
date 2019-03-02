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
import edu.gatech.chai.pacer.model.Organization;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-02-26T15:28:40.714830-05:00[America/New_York]")
@Controller
public class ManageApiController implements ManageApi {

	private static final Logger log = LoggerFactory.getLogger(ManageApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@org.springframework.beans.factory.annotation.Autowired
	private PacerDaoImpl pacerDao;

	@org.springframework.beans.factory.annotation.Autowired
	public ManageApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	public ResponseEntity<Void> addOrganization(
			@ApiParam(value = "Organization info to add") @Valid @RequestBody Organization body) {
		String accept = request.getHeader("Accept");

		Organizations existingOrgs = pacerDao.getByIdentifier(body.getIdentifier());
		if (existingOrgs.getCount() > 0) {
			// This is error.
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		if (body.getId() != null && body.getId() > 0) {
			Organization existingOrg = pacerDao.getById(body.getId());
			if (existingOrg != null) {
				existingOrg.setName(body.getName());
				existingOrg.setIdentifier(body.getIdentifier());
				existingOrg.setPacerSource(body.getPacerSource());

				pacerDao.update(existingOrg);

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				return new ResponseEntity<>(headers, HttpStatus.OK);
			}
		}

		int id = pacerDao.save(body);
		if (id > 0) {
			URI location = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/manage/{id}").build()
					.expand(id).toUri();
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(location);

			return new ResponseEntity<>(headers, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Void> deleteOrganization(
			@ApiParam(value = "Organization ID to be deleted", required = true) @PathVariable("id") Integer id) {
		String accept = request.getHeader("Accept");

		Organization organization = pacerDao.getById(id);
		if (organization == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			pacerDao.delete(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}

	public ResponseEntity<Organization> getOrganization(
			@ApiParam(value = "Get a organization by ID", required = true) @PathVariable("id") Integer id) {
		String accept = request.getHeader("Accept");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Organization organization = pacerDao.getById(id);
		if (organization == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Organization>(organization, headers, HttpStatus.OK);
		}
	}

	public ResponseEntity<Organizations> getOrganizations() {
		String accept = request.getHeader("Accept");
				
		Organizations organizations = pacerDao.get();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return new ResponseEntity<Organizations>(organizations, headers, HttpStatus.OK);
	}

	public ResponseEntity<Void> updateOrganization(
			@ApiParam(value = "Organization ID to be updated", required = true) @PathVariable("id") Integer id,
			@ApiParam(value = "Organization info to add") @Valid @RequestBody Organization body) {
		String accept = request.getHeader("Accept");
		
		Organization organization = pacerDao.getById(id);
		if (organization == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			pacerDao.update(body);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}

}
