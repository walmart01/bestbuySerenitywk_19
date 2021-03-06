package com.bestbuyplayground.productinfo;

import com.bestbuyplayground.bestbuyinfo.ProductsSteps;
import com.bestbuyplayground.constants.EndPoints;
import com.bestbuyplayground.model.ProductPojo;
import com.bestbuyplayground.testbase.TestBase;
import com.bestbuyplayground.utilis.TestUtils;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
    public class ProductCRUDTestWithSteps extends TestBase {

        static String name = "Energiser" + TestUtils.getRandomValue();
        static String type = "Whitegood" + TestUtils.getRandomValue();
        static double price = 7.99;
        static int shipping = 1 + TestUtils.getRandomIntegerValue();
        static String upc = "041333415017" + TestUtils.getRandomValue();
        static String description = "Compatible with select electronic devices" + TestUtils.getRandomValue();
        static String manufacturer = "Duracell" + TestUtils.getRandomValue();
        static String model = "VD" + TestUtils.getRandomValue();
        static String url = "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC" + TestUtils.getRandomValue();
        static String image = "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa" + TestUtils.getRandomValue() + ".jpg";
        static int productsId;

        @Steps
        ProductsSteps productsSteps;

        @Title("Creating and verify new product is added to product application")
        @Test
        public void test001() {

            HashMap<String, Object> pvalue = productsSteps.createProduct(name, type, price, shipping, upc, description, manufacturer, model, url, image)
                    .log().all().statusCode(201).extract().response().body().jsonPath().get();
            System.out.println(pvalue);
            assertThat(pvalue, hasValue(name));
            productsId = (int) pvalue.get("id");
            System.out.println(productsId);

        }

        @Title("Update the product information and verify the updated information")
        @Test
        public void test002() {
            name = name + "_Updated";

            ProductPojo productsPojo = new ProductPojo();
            productsPojo.setName(name);
            productsPojo.setType(type);
            productsPojo.setPrice(price);
            productsPojo.setShipping(shipping);
            productsPojo.setUpc(upc);
            productsPojo.setDescription(description);
            productsPojo.setManufacturer(manufacturer);
            productsPojo.setModel(model);
            productsPojo.setUrl(url);
            productsPojo.setImage(image);

            SerenityRest.rest().given().log().all()
                    .header("Content-Type", "application/json")
                    .pathParam("productsId", productsId)
                    .body(productsPojo)
                    .when()
                    .put(EndPoints.UPDATE_PRODUCT_BY_ID)
                    .then().log().all().statusCode(200);

            HashMap<String, Object> pvalue = SerenityRest.rest().given().log().all()
                            .when()
                            .get(EndPoints.GET_ALL_PRODUCTS)
                            .then()
                            .statusCode(200)
                            .extract().response().body().jsonPath().get();
            assertThat(pvalue, hasValue(name));

        }

        @Title("Deleting and verify the product is deleted")
        @Test
        public void test003() {
            SerenityRest.rest().given().log().all()
                    .pathParam("productsId", productsId)
                    .when()
                    .delete(EndPoints.DELETE_PRODUCT_BY_ID)
                    .then()
                    .statusCode(200);

            SerenityRest.rest().given().log().all()
                    .pathParam("productsId", productsId)
                    .when()
                    .get(EndPoints.GET_SINGLE_PRODUCT_BY_ID)
                    .then()
                    .statusCode(404);
        }
    }








