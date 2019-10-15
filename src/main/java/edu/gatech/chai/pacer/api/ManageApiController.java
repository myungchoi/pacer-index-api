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
import edu.gatech.chai.pacer.model.PacerSource;
import edu.gatech.chai.pacer.model.SecurityForPacer;

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
import javax.annotation.PostConstruct;
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

	public static String LOCAL_PROVIDER_NAME = "LOCAL PROVIDER";
	public static String LOCAL_PROVIDER_ID = "LOCAL_PROVIDER|1";
	public static String LOCAL_PACER_NAME = "LOCAL_PACER";
	public static String LOCAL_PACER_VERSION = "1.4.3";
	public static PacerSource.TypeEnum LOCAL_PACER_TYPE = PacerSource.TypeEnum.ECR;

	@org.springframework.beans.factory.annotation.Autowired
	public ManageApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}
	
	@PostConstruct
	public void initialLoad() {
		// Pre populate the PACER index url for local use if environment variables are
		// available.
		String localPacerUrl = System.getenv("LOCAL_PACER_URL");
		String localPacerSecurity = System.getenv("LOCAL_PACER_SECURITY");
		String localPacerVersion = System.getenv("LOCAL_PACER_VERSION");
		String localPacerType = System.getenv("LOCAL_PACER_TYPE");

		if (localPacerUrl != null && !localPacerUrl.isEmpty()) {
			Organizations existingOrgs = pacerDao.getByProviderNameAndIdentifier(
					ManageApiController.LOCAL_PROVIDER_NAME, ManageApiController.LOCAL_PROVIDER_ID);
			if (existingOrgs.getCount() == 0) {
				Organization defaultLocalOrg = new Organization();
				defaultLocalOrg.setProviderName(ManageApiController.LOCAL_PROVIDER_NAME);
				defaultLocalOrg.setIdentifier(ManageApiController.LOCAL_PROVIDER_ID);

				PacerSource defaultPacerSource = new PacerSource();
				defaultPacerSource.setName(ManageApiController.LOCAL_PACER_NAME);
				defaultPacerSource.setServerUrl(localPacerUrl);

				if (localPacerVersion != null && !localPacerVersion.isEmpty())
					defaultPacerSource.setVersion(localPacerVersion);
				else
					defaultPacerSource.setVersion(ManageApiController.LOCAL_PACER_VERSION);

				if (localPacerType != null && !localPacerType.isEmpty())
					defaultPacerSource.setType(PacerSource.TypeEnum.fromValue(localPacerType));
				else
					defaultPacerSource.setType(ManageApiController.LOCAL_PACER_TYPE);

				SecurityForPacer defaultSecurityPacer = null;
				if (localPacerSecurity != null && !localPacerSecurity.isEmpty()) {
					String[] securityInfo = localPacerSecurity.split(" ");
					if (securityInfo.length == 2) {
						if ("basic".equalsIgnoreCase(securityInfo[0])) {
							String[] credential = securityInfo[1].split(":");
							if (credential.length == 2) {
								defaultSecurityPacer = new SecurityForPacer();
								defaultSecurityPacer.setType("basic");
								defaultSecurityPacer.setUsername(credential[0]);
								defaultSecurityPacer.setPassword(credential[1]);
							}
						} else if ("brearer".equalsIgnoreCase(securityInfo[0])) {
							defaultSecurityPacer = new SecurityForPacer();
							defaultSecurityPacer.setType("bearer");
							defaultSecurityPacer.setUsername(securityInfo[1]);
						}
					}
				}
				
				if (defaultSecurityPacer != null) {
					defaultPacerSource.setSecurity(defaultSecurityPacer);
				}
				
				defaultLocalOrg.setPacerSource(defaultPacerSource);
				
				pacerDao.save(defaultLocalOrg);
			}
		}
	}

	public ResponseEntity<Void> addOrganization(
			@ApiParam(value = "Organization info to add") @Valid @RequestBody Organization body) {
		String accept = request.getHeader("Accept");

		Organizations existingOrgs = pacerDao.getByProviderNameAndIdentifier(body.getProviderName(),
				body.getIdentifier());
		if (existingOrgs.getCount() > 0) {
			// This is error.
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		if (body.getId() != null && body.getId() > 0) {
			Organization existingOrg = pacerDao.getById(body.getId());
			if (existingOrg != null) {
				existingOrg.setProviderName(body.getProviderName());
				existingOrg.setIdentifier(body.getIdentifier());
				existingOrg.setPacerSource(body.getPacerSource());

				log.debug("POST found existing organization/provider (" + body.getIdentifier() + "/"
						+ body.getProviderName() + ")");
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
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			pacerDao.update(body);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}

}
