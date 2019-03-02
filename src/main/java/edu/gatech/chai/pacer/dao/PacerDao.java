package edu.gatech.chai.pacer.dao;

import java.sql.Connection;
import java.util.List;

import edu.gatech.chai.pacer.model.Organization;
import edu.gatech.chai.pacer.model.Organizations;

public interface PacerDao {
	public Connection connect();

	public int save(Organization organization);
	public void update(Organization organization);
	public void delete(Integer id);
	public Organizations get();
	public Organization getById(Integer id);
	public Organization getByName(String name);
	public Organization getByIdentifier(String identifier);
}
