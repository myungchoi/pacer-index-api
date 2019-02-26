package edu.gatech.chai.pacer.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import edu.gatech.chai.pacer.model.Organization;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Organizations
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-02-26T15:28:40.714830-05:00[America/New_York]")
public class Organizations   {
  @JsonProperty("count")
  private Integer count = null;

  @JsonProperty("created")
  private OffsetDateTime created = null;

  @JsonProperty("list")
  @Valid
  private List<Organization> list = new ArrayList<Organization>();

  public Organizations count(Integer count) {
    this.count = count;
    return this;
  }

  /**
   * Get count
   * @return count
  **/
  @ApiModelProperty(example = "1", required = true, value = "")
  @NotNull

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Organizations created(OffsetDateTime created) {
    this.created = created;
    return this;
  }

  /**
   * Get created
   * @return created
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
  public OffsetDateTime getCreated() {
    return created;
  }

  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  public Organizations list(List<Organization> list) {
    this.list = list;
    return this;
  }

  public Organizations addListItem(Organization listItem) {
    this.list.add(listItem);
    return this;
  }

  /**
   * Get list
   * @return list
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  @Valid
  public List<Organization> getList() {
    return list;
  }

  public void setList(List<Organization> list) {
    this.list = list;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Organizations organizations = (Organizations) o;
    return Objects.equals(this.count, organizations.count) &&
        Objects.equals(this.created, organizations.created) &&
        Objects.equals(this.list, organizations.list);
  }

  @Override
  public int hashCode() {
    return Objects.hash(count, created, list);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Organizations {\n");
    
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    list: ").append(toIndentedString(list)).append("\n");
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
