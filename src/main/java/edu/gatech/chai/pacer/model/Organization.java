package edu.gatech.chai.pacer.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import edu.gatech.chai.pacer.model.PacerSource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Organization
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-03-07T17:45:31.984763-05:00[America/New_York]")
public class Organization   {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("providerName")
  private String providerName = null;

  @JsonProperty("identifier")
  private String identifier = null;

  @JsonProperty("pacerSource")
  private PacerSource pacerSource = null;

  public Organization id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(example = "1", value = "")

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Organization providerName(String providerName) {
    this.providerName = providerName;
    return this;
  }

  /**
   * Get providerName
   * @return providerName
  **/
  @ApiModelProperty(example = "Patch Adams", value = "")

  public String getProviderName() {
    return providerName;
  }

  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }

  public Organization identifier(String identifier) {
    this.identifier = identifier;
    return this;
  }

  /**
   * Get identifier
   * @return identifier
  **/
  @ApiModelProperty(example = "type|id", required = true, value = "")
  @NotNull

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public Organization pacerSource(PacerSource pacerSource) {
    this.pacerSource = pacerSource;
    return this;
  }

  /**
   * Get pacerSource
   * @return pacerSource
  **/
  @ApiModelProperty(value = "")

  @Valid
  public PacerSource getPacerSource() {
    return pacerSource;
  }

  public void setPacerSource(PacerSource pacerSource) {
    this.pacerSource = pacerSource;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Organization organization = (Organization) o;
    return Objects.equals(this.id, organization.id) &&
        Objects.equals(this.providerName, organization.providerName) &&
        Objects.equals(this.identifier, organization.identifier) &&
        Objects.equals(this.pacerSource, organization.pacerSource);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, providerName, identifier, pacerSource);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Organization {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    providerName: ").append(toIndentedString(providerName)).append("\n");
    sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
    sb.append("    pacerSource: ").append(toIndentedString(pacerSource)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
