package edu.gatech.chai.pacer.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PacerSource
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-02-26T16:50:41.046686-05:00[America/New_York]")
public class PacerSource   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("serverUrl")
  private String serverUrl = null;

  @JsonProperty("security")
  private Object security = null;

  @JsonProperty("version")
  private String version = null;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    ECR("ECR"),
    
    FHIR("FHIR");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("type")
  private TypeEnum type = null;

  public PacerSource name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(example = "PACER name", required = true, value = "")
  @NotNull

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PacerSource serverUrl(String serverUrl) {
    this.serverUrl = serverUrl;
    return this;
  }

  /**
   * Get serverUrl
   * @return serverUrl
  **/
  @ApiModelProperty(example = "https://apps.hdap.gatech.edu/pacer/", required = true, value = "")
  @NotNull

  public String getServerUrl() {
    return serverUrl;
  }

  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
  }

  public PacerSource security(Object security) {
    this.security = security;
    return this;
  }

  /**
   * place holder for future security model sepcification for FHIR server
   * @return security
  **/
  @ApiModelProperty(example = "{\"type\":\"basic\",\"username\":\"username\",\"password\":\"password\"}", value = "place holder for future security model sepcification for FHIR server")

  public Object getSecurity() {
    return security;
  }

  public void setSecurity(Object security) {
    this.security = security;
  }

  public PacerSource version(String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
  **/
  @ApiModelProperty(example = "1.0.0", value = "")

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public PacerSource type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")

  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PacerSource pacerSource = (PacerSource) o;
    return Objects.equals(this.name, pacerSource.name) &&
        Objects.equals(this.serverUrl, pacerSource.serverUrl) &&
        Objects.equals(this.security, pacerSource.security) &&
        Objects.equals(this.version, pacerSource.version) &&
        Objects.equals(this.type, pacerSource.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, serverUrl, security, version, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PacerSource {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    serverUrl: ").append(toIndentedString(serverUrl)).append("\n");
    sb.append("    security: ").append(toIndentedString(security)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
