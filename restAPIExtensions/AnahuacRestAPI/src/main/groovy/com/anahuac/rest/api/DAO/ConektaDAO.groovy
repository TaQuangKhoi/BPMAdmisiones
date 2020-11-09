package com.anahuac.rest.api.DAO

import org.bonitasoft.engine.bpm.process.ProcessDefinition
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.Entity.ConektaOxxo
import com.anahuac.rest.api.Entity.ConektaPaymentResponse
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper
import io.conekta.Charge
import io.conekta.Conekta
import io.conekta.Customer
import io.conekta.LineItems
import io.conekta.Order
import io.conekta.OxxoPayment
import io.conekta.PaymentMethod

class ConektaDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConektaDAO.class);

    public Result pagoOxxoCash(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        List < ConektaOxxo > lstResultado = new ArrayList < ConektaOxxo > ();
        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;

        Integer start = 0;
        Integer end = 99999;

        Boolean isFound = true;

        String nombreCandidato = "";



        try {
            /*def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            assert object instanceof Map;
            if (object.lstFiltro != null) {
                assert object.lstFiltro instanceof List;
            }

            userLogged = context.getApiSession().getUserId();*/

            Long nowUnixTimestamp = System.currentTimeMillis();
            Long thirtyDaysFromNowUnixTimestamp = (nowUnixTimestamp + 30L * 24 * 60 * 60 * 1000)/ 1000L;
            String thirtyDaysFromNow = thirtyDaysFromNowUnixTimestamp.toString();
			Conekta.setApiKey("key_dHs4rzK9otyxcbqTrzn5sw");
			
            Order order = Order.create(
                new JSONObject("{" +
                    "'line_items': [{" +
                    "'name': 'Adminsión'," +
                    "'unit_price': 1000," +
                    "'quantity': 1" +
                    "}]," +
                    "'currency': 'MXN'," +
                    "'customer_info': {" +
                    "'name': 'Manuel Pérez'," +
                    "'email': 'manuel@conekta.com'," +
                    "'phone': '+5218181818181'" +
                    "}," +
                    "'shipping_contact':{" +
                    "'address': {" +
                    "'street1': 'Calle 123, int 2'," +
                    "'postal_code': '06100'," +
                    "'country': 'MX'" +
                    "}" +
                    "}," //shipping_contact - required only for physical goods
                    +
                    "'charges':[{" +
                    "'payment_method': {" +
                    "'type': 'oxxo_cash'," +
                    "'expires_at': " + thirtyDaysFromNow +
                    "}" +
                    "}]" +
                    "}"
                )
            );
            LineItems line_item = (LineItems) order.line_items.get(0);
            Charge charge = (Charge) order.charges.get(0);
            OxxoPayment oxxoPayment = (OxxoPayment) charge.payment_method;

            lstResultado.add(new ConektaOxxo(
                order.id,
                "" + (order.amount / 100) + order.currency,
                line_item.quantity + " - " + line_item.name + " - " + (line_item.unit_price / 100),
                oxxoPayment.reference));
            LOGGER.error "ID: " + order.id;
            LOGGER.error "Payment Method: " + oxxoPayment.service_name;
            LOGGER.error "Reference: " + oxxoPayment.reference;
            LOGGER.error "" + (order.amount / 100) + order.currency;
            LOGGER.error "Order";
            LOGGER.error line_item.quantity + " - " + line_item.name + " - " + (line_item.unit_price / 100);
            resultado.setData(lstResultado);
            resultado.setSuccess(true);
        } catch (Exception e) {
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            LOGGER.error "ERROR=================================";
            LOGGER.error e.getMessage();
            e.printStackTrace();
        }
        return resultado
    }
	
	public Result pagoTarjeta(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		List<ConektaPaymentResponse> lstResultado = new ArrayList<ConektaPaymentResponse>();
		//Inicialización del cliente Conekta a través de la adición de la llave privada y versión del API.
		Conekta.setApiKey("key_dHs4rzK9otyxcbqTrzn5sw"); //  <-- Mock private key, please use YOUR personal private key
		//com.conekta.Conekta.apiVersion = "2.0.0"
		String token = "";

		//Para los siguientes pasos en la integración un token_id de prueba se provee.
		//Para tener acceso al token que se generó en el paso 1.0 → es necesario accesarlo en el objeto conektaSuccessResponseHandler["id"]
		//Generación del cliente y la información de pago.
		try{
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			token = object.idToken;
			LOGGER.error token;
		  Customer customer = Customer.create(
			new JSONObject("{"
			  + "'name': 'Fulanito Pérez', "
			  + "'email': 'fulanito@conekta.com',"
			  + "'phone': '+52181818181',"
			  + "'metadata': {'description': 'Compra de creditos: 300(MXN)' , 'reference' : '1334523452345'},"
			  + "'payment_sources':[{"
				+ "'type': 'card',"
				+ "'token_id': '"+token+"'"
				+ "}]"
			  + "}"
			)
		  );

		  LOGGER.error token+" 1";
		//Implementación de una orden.
		Order order = Order.create(
			new JSONObject("{"
			  + "'line_items': [{"
				  + "'name': 'Admisión',"
				  + "'unit_price': 1000,"
				  + "'quantity': 1"
			  + "}]," //line_items
			  + "'currency': 'MXN',"
			  + "'customer_info': {"
				+ "'customer_id': '"+customer.getId()+"'"
			  + "}," //customer_info
			  + "'shipping_contact':{"
				 + "'address': {"
				   + "'street1': 'Calle 123, int 2',"
				   + "'postal_code': '06100',"
				   + "'country': 'MX'"
				 + "}"
			   + "}," //shipping_contact - required only for physical goods
			   +"'metadata': {'description': 'Compra de creditos: 300(MXN)' , 'reference' : '1334523452345'},"
			  + "'charges':[{"
				+ "'payment_method': {"
				  + "'type': 'default'"
				+ "}" //payment_method - use the customer's default - a card
					  //to charge a card, different from the default,
					  //you can indicate the card's source_id as shown in the Retry Card Section
			  + "}]" //charges
			+ "}" //order
			)
		  );
		  LOGGER.error token+" 2";

		  LineItems line_item = (LineItems)order.line_items.get(0);
		  Charge charge = (Charge) order.charges.get(0);
		  PaymentMethod payment_method = (PaymentMethod) charge.payment_method;

		  LOGGER.error "ID: " + order.id
		  LOGGER.error "Status: " + order.payment_status
		  LOGGER.error "" + (order.amount/100) + order.currency
		  LOGGER.error "Order"
		  LOGGER.error line_item.quantity + " - " + line_item.name + " - " + (line_item.unit_price/100)
		  LOGGER.error "Payment info"
		  LOGGER.error "Code: " + payment_method.getVal("auth_code")
		  LOGGER.error "Card info: " + payment_method.getVal("name") + " - " + payment_method.getVal("last4") + " - " + payment_method.getVal("brand") + " - " + payment_method.getVal("type")
		  
		  lstResultado.add(new ConektaPaymentResponse(order.id, "" + (order.amount/100) + order.currency, line_item.quantity + " - "
							+ line_item.name + " - "
							+ (line_item.unit_price/100), payment_method.getVal("auth_code")+"", payment_method.getVal("name") + " - "
							+ payment_method.getVal("last4") + " - "
							+ payment_method.getVal("brand") + " - "
							+ payment_method.getVal("type")));
		  resultado.setData(lstResultado)
		  resultado.setSuccess(true)
		  
		}catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
		    System.out.println(e.getMessage());
		}
		return resultado;
	
	}
}