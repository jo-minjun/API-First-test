package minjun.openapigenerator.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * some description 
 */

@Schema(name = "Amount", description = "some description ")
@JsonTypeName("Amount")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class AmountDto {

  @JsonProperty("value")
  private Double value;

  @JsonProperty("currency")
  private String currency;

  public AmountDto value(Double value) {
    this.value = value;
    return this;
  }

  /**
   * some description 
   * minimum: 0.01
   * maximum: 1E+15
   * @return value
  */
  @NotNull @DecimalMin("0.01") @DecimalMax("1E+15") 
  @Schema(name = "value", description = "some description ", required = true)
  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public AmountDto currency(String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * some description 
   * @return currency
  */
  @NotNull @Pattern(regexp = "^[A-Z]{3,3}$") 
  @Schema(name = "currency", description = "some description ", required = true)
  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AmountDto amount = (AmountDto) o;
    return Objects.equals(this.value, amount.value) &&
        Objects.equals(this.currency, amount.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, currency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AmountDto {\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

