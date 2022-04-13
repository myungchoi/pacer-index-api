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
package edu.gatech.chai.pacer.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.threeten.bp.OffsetDateTime;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.gatech.chai.pacer.model.Organization;
import edu.gatech.chai.pacer.model.Organizations;
import edu.gatech.chai.pacer.model.PacerSource;
import edu.gatech.chai.pacer.model.PacerSource.TypeEnum;
import edu.gatech.chai.pacer.model.SecurityForPacer;

@Component
public class PacerDaoImpl implements PacerDao {
	final static Logger logger = LoggerFactory.getLogger(PacerDaoImpl.class);

	@Override
	public Connection connect() {
		String dbPath = System.getenv("DB_PATH");
		String url;
		if (dbPath == null || dbPath.isEmpty()) {
			url = "jdbc:sqlite::resource:PIDB.db";
		} else {
			url = "jdbc:sqlite:" + dbPath.trim();
		}
		
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(url);
			logger.info("Connected to database");
		} catch (SQLException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return conn;
	}

	private int getPacerSourceId(PacerSource pacerSource) {
		int retv = 0;

		String sql = "SELECT * FROM pacer_source WHERE name=? and server_url=? and type=?";
		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, pacerSource.getName());
			pstmt.setString(2, pacerSource.getServerUrl());
			pstmt.setString(3, pacerSource.getType().toString());

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				retv = rs.getInt("id");
				logger.info("pacer (" + retv + ") selected for pacer endpoint info");
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		if (retv == 0) {
			retv = savePacerSource(pacerSource);
		}

		return retv;
	}

	private int savePacerSource(PacerSource pacerSource) {
		String sql = "INSERT INTO pacer_source (name, server_url, version, type, security) values (?,?,?,?,?)";

		int insertedId = 0;
		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, pacerSource.getName());
			pstmt.setString(2, pacerSource.getServerUrl());
			pstmt.setString(3, pacerSource.getVersion());
			pstmt.setString(4, pacerSource.getType().toString());

			// security is JSON object.
			SecurityForPacer securityObject = pacerSource.getSecurity();
			if (securityObject != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				pstmt.setString(5, objectMapper.writeValueAsString(securityObject));
			} else {
				pstmt.setString(5, null);
			}

			if (pstmt.executeUpdate() > 0) {
				// Retrieves any auto-generated keys created as a result of executing this
				// Statement object
				java.sql.ResultSet generatedKeys = pstmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					insertedId = generatedKeys.getInt(1);
				}
			}

			logger.info("New pacer source data (id=" + insertedId + ") added");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return insertedId;
	}

	@Override
	public int save(Organization organization) {
		String sql = "INSERT INTO organization (provider_name, identifier, pacer_source_id) values (?,?,?)";

		int insertedId = 0;
		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, organization.getProviderName());
			pstmt.setString(2, organization.getIdentifier());

			int pacerId = getPacerSourceId(organization.getPacerSource());
			pstmt.setInt(3, pacerId);

			if (pstmt.executeUpdate() > 0) {
				// Retrieves any auto-generated keys created as a result of executing this
				// Statement object
				java.sql.ResultSet generatedKeys = pstmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					insertedId = generatedKeys.getInt(1);
				}
			}

			logger.info("New filter data (id=" + insertedId + ") added");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return insertedId;
	}

	private void orphanDelete() {
		String sql = "DELETE FROM pacer_source WHERE NOT EXISTS (SELECT * FROM organization o WHERE o.pacer_source_id = pacer_source.id)";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.executeUpdate();

			logger.info("Orphan pacer_source(s) deleted.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void update(Organization organization) {
		String sql = "UPDATE organization SET provider_name=?, identifier=?, pacer_source_id=? WHERE id=?";

		// First get the pacer_source_id.
		PacerSource pacerSource = organization.getPacerSource();
		int pacerSourceId = getPacerSourceId(pacerSource);

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, organization.getProviderName());
			pstmt.setString(2, organization.getIdentifier());
			pstmt.setInt(3, pacerSourceId);
			pstmt.setInt(4, organization.getId());
			pstmt.executeUpdate();

			logger.info("Organization data (" + organization.getId() + ") updated.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		// Remove orphan entries in PACER table
		orphanDelete();
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM organization WHERE id = ?";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			logger.info("organization data (" + id + ") deleted");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		orphanDelete();
	}

	private PacerSource getPacerSourceById(Integer id) {
		PacerSource pacerSource = new PacerSource();
		String sql = "SELECT * FROM pacer_source WHERE id=?";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				pacerSource.setName(rs.getString("name"));
				pacerSource.setServerUrl(rs.getString("server_url"));
				pacerSource.setVersion(rs.getString("version"));
				pacerSource.setType(TypeEnum.valueOf(rs.getString("type")));
				String securityString = rs.getString("security");
				if (securityString != null && !securityString.isEmpty()) {
					ObjectMapper objectMapper = new ObjectMapper();
					SecurityForPacer securityObject = objectMapper.readValue(securityString, SecurityForPacer.class);
					pacerSource.setSecurity(securityObject);
				}
			}
			logger.info("Pacer Rsource (" + id + ") data obtained");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pacerSource;
	}

	private Organization setOrganizationData(ResultSet rs) throws SQLException {
		Organization organization = new Organization();
		organization.setId(rs.getInt("id"));
		organization.setProviderName(rs.getString("provider_name"));
		organization.setIdentifier(rs.getString("identifier"));
		organization.setPacerSource(getPacerSourceById(rs.getInt("pacer_source_id")));

		return organization;
	}

	@Override
	public Organizations get() {
		Organizations organizations = new Organizations();
		List<Organization> organizationList = new ArrayList<Organization>();

		String sql = "SELECT * FROM organization";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Organization organization = setOrganizationData(rs);
				organizationList.add(organization);
			}
			logger.info(organizationList.size() + " organization(s) data obtained");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		organizations.setList(organizationList);
		organizations.setCount(organizationList.size());
		organizations.setCreated(OffsetDateTime.now());

		return organizations;
	}

	@Override
	public Organization getById(Integer id) {
		Organization organization = null;
		String sql = "SELECT * FROM organization WHERE id = ?";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				organization = setOrganizationData(rs);
			}
			logger.info("organzation (" + id + ") selected");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return organization;
	}

	@Override
	public Organizations getByProviderName(String providerName) {
		Organizations organizations = new Organizations();
		List<Organization> orgList = new ArrayList<Organization>();
		
		String sql = "SELECT * FROM organization WHERE provider_name = ?";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, providerName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				orgList.add(setOrganizationData(rs));
			}
			
			organizations.setList(orgList);
			organizations.setCount(orgList.size());
			organizations.setCreated(OffsetDateTime.now());
			logger.info("organization (" + providerName + ") selected");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return organizations;
	}

	@Override
	public Organizations getByIdentifier(String identifier) {
		Organizations organizations = new Organizations();
		List<Organization> orgList = new ArrayList<Organization>();
		
		String sql = "SELECT * FROM organization WHERE identifier = ?";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, identifier);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				orgList.add(setOrganizationData(rs));
			}
			
			organizations.setList(orgList);
			organizations.setCount(orgList.size());
			organizations.setCreated(OffsetDateTime.now());
			logger.info("organization with identifier= (" + identifier + ") selected");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return organizations;
	}

	@Override
	public Organizations getByProviderNameAndIdentifier(String providerName, String identifier) {
		Organizations organizations = new Organizations();
		List<Organization> orgList = new ArrayList<Organization>();
		
		String sql = "SELECT * FROM organization where provider_name = ? AND identifier = ?";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, providerName);
			pstmt.setString(2, identifier);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				orgList.add(setOrganizationData(rs));
			}
			
			organizations.setList(orgList);
			organizations.setCount(orgList.size());
			organizations.setCreated(OffsetDateTime.now());
			logger.info(orgList.size()+" organization(s) with provider name= (" + providerName + "), identifier= (" + identifier + ") selected");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return organizations;
	}

}
