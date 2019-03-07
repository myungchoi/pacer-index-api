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
	public Organizations getByProviderName(String providerName);
	public Organizations getByProviderNameAndIdentifier(String providerName, String identifier);
	public Organizations getByIdentifier(String identifier);
}
