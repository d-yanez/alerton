/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.home.alertaProductos.rest.services.impl;

import cl.home.alertaProductos.rest.services.CrawlerServices;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.DataNode;

/**
 *
 * @author ext_dayanez
 */
@Service
public class CrawlerServicesImpl implements CrawlerServices{
    private static final Logger log = LoggerFactory.getLogger(CrawlerServicesImpl.class);
    
    //@Autowired(required = true)
    private  static HashSet<String> links = new HashSet<>();
    
    private boolean esUrlProductoValida(String url){
        
        Pattern p = Pattern.compile("/product/|/category/");
         Matcher m = p.matcher(url);
         return m.find();
    }
    private boolean esUrlFalabella(String url){
    
        return url.contains("www.falabella.com/falabella-cl");
    }

    @Override
    public void getInfoProductos(String urlProducto) {
       //& Document document = Jsoup.connect(urlProducto).get();
        //3. Parse the HTML to extract links to other URLs
        //Elements linksOnPage = document.select("a[href]");
//        Element prodContainer = document.select("div.fb-product-cta__container").first();
//        if (prodContainer != null){
//            //5. For each extracted URL... go back to Step 4.
//            Element prodName = prodContainer.select(urlProducto)
//            for (Element page : linksOnPage) {                                
//                getPageLinks(page.attr("abs:href"));
//            }
//        }
    }
    
    
    //Web Crawler Básico
    @Override
    public void getPageLinks(String URL) {
        
//        if(links == null){
//            links = new HashSet<>();
//        }
        //4. Check if you have already crawled the URLs 
        //(e are intentionally not checking for duplicate content in this example)
        if (!"".equals(URL) && !links.contains(URL)) {
            try {
                if(esUrlFalabella(URL)){
                    if(esUrlProductoValida(URL)){
                                    //4. (i) If not add it to the index
                        if (links.add(URL)) {
                            log.info("{} agregada!!",URL);
                        }

                        //2. Fetch the HTML code
                        Document document = Jsoup.connect(URL).get();
                        //3. Parse the HTML to extract links to other URLs
                        Elements linksOnPage = document.select("a[href]");
                        if (linksOnPage != null && linksOnPage.size() > 0){
                            //5. For each extracted URL... go back to Step 4.
                            for (Element page : linksOnPage) {                                
                                getPageLinks(page.attr("abs:href"));
                            }
                        }
                    }
                    else{
                        log.info("URL ->{} No es de producto!!",URL);
                    }
                }
                else{
                        log.info("URL ->{} No es de fabella",URL);
                }

            } catch (IOException e) {
                log.error("For '" + URL + "': " + e.getMessage(),e);
            }
        }
        else{
            log.info("{} ya encontrada!!",URL);
        }
    }

    @Override
    public void test(String mensaje) {
       log.info("Hi from ->{}",mensaje);
    }
    public  void savePage(String URL){
        String line = "";
        String all = "";
        URL myUrl = null;
        BufferedReader in = null;
        log.info("downloding....==>{}",URL);
        try {
            myUrl = new URL(URL);
            in = new BufferedReader(new InputStreamReader(myUrl.openStream()));

            while ((line = in.readLine()) != null) {
                all += line;
            }
        } 
        catch(IOException e){
            log.error("error: ",e);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        log.info("html==>{}",all);
        //return all;
    }
    public void getHtml(String url){
        try {
            log.info("url==>{}",url);
            String html = Jsoup.connect(url).timeout(10000).get().html();
            log.info("html==>{}",html);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void spider(String urlFalabella){
        try {
            //log.info("spider");
            log.info("urlFalabella==>{}",urlFalabella);
            //Document document = Jsoup.connect("https://www.falabella.com/falabella-cl/product/5846832/LED-49-49MU6500-4K-Ultra-HD-Smart-TV-Curvo/5846832").timeout(10000).get();
            Document document = Jsoup.connect(urlFalabella).timeout(10000).get();
            
            //String html = Jsoup.connect("https://www.falabella.com/falabella-cl/product/5846832/LED-49-49MU6500-4K-Ultra-HD-Smart-TV-Curvo/5846832").get().html();
            //3. Parse the HTML to extract links to other URLs
            //Elements tituloProducto = document.select("body");
            //log.info("html: {}",html);
            Elements scriptElements = document.getElementsByTag("script");
            StringBuilder html2 = new StringBuilder();
            for (Element element :scriptElements ){                
                   for (DataNode node : element.dataNodes()) {
                       
                       if(node.getWholeData().contains("controlParameter")){
                           String[] l = node.getWholeData().split("\\n");
                           for(String a : l){
                               if(a.contains("controlParameter")){
                                   html2.append(a);
                               }
                           }
                           //log.info(node.getWholeData());
                           
                       }
                       //System.out.println(node.getWholeData());
                   }
                   //System.out.println("-------------------");            
             }
           
            //log.info(System.getProperty("user.dir"));
            
            //log.info("json==>{}",html2.toString());
            String[] pa=html2.toString().split("fbra_browseMainProductConfig =");
            
            String json = pa[1].trim();
            json = json.substring(0, json.length()-1);
            //log.info("json->"+json);
            JSONParser parser = new JSONParser();
            try {
                JSONObject  jsonProductConfig  = (JSONObject)parser.parse(json);
                JSONObject stateJson = (JSONObject)jsonProductConfig.get("state");
                JSONObject productJson = (JSONObject)stateJson.get("product");
                String sku = (String)productJson.get("id");
                String descripcion = (String)productJson.get("displayName");
                String marca = (String)productJson.get("brand");
                JSONArray jSonPrecios = (JSONArray)productJson.get("prices");
                log.info("sku->"+sku);
                log.info("descripcion->"+descripcion);
                log.info("marca->"+marca);
                for(int  i = 0; i< jSonPrecios.size(); i++){
                    JSONObject p = (JSONObject)jSonPrecios.get(i);
                    Long type = (Long)p.get("type");
                    if(type.compareTo(new Long(3)) == 0){
                        log.info("precio oferta->"+p.get("originalPrice"));
                    }
                    if(type.compareTo(new Long(1)) == 0){
                        log.info("precio oferta (con CMR)->"+p.get("originalPrice"));
                    } 
                    if(type.compareTo(new Long(2)) == 0){
                        log.info("precio normal->"+p.get("originalPrice"));
                    }
                }
                 //log.info("maxQuantity->"+maxQuantity);
//            String json =pa[1].trim();// "{\"controlParameter\":{\"enableLowStock\":false,\"enableBazaarVoice\":true,\"enableDeliveryOptions\":true,\"enableOneClick\":false,\"maxQuantity\":10,\"enableCMRInstalment\":true},\"componentDomId\":\"fbra_browseMainProduct\",\"textDictionary\":{\"disclaimerText\":\"<p>Las figuras son solo informativas.</p><p>Podrás escoger tu medio de pago más adelante.</p>\",\"extendedWarrantyBenefitText\":\"Con <b>Garantía Extendida</b> tus productos favoritos están protegidos por mucho más tiempo, siéntete tranquilo, tu producto ya está asegurado.\",\"quantityLabelText\":\"cantidad\",\"deliverToAddressText\":\"Despacho a domicilio\",\"sizeGuideTitleText\":\"Tabla de tallas\",\"deliveryDetailsText\":\"Compra en los próximos {0} {1} para despacho en 24 horas.\",\"totalSubText\":\"Costo total del crédito\",\"codeProductText\":\"Codigo producto\",\"selectBicycleServiceErrorText\":\"Por favor selecciona una opción\",\"notEnoughStockText\":\"Lo sentimos, no hay suficiente unidades disponibles\",\"isCODAvailableText\":\"disponible pago contra entrega\",\"extendedWarrantyReasonTwo\":\"Te aseguramos el uso de repuestos originales.\",\"financeCalculatorPreTitleText\":\"Quiero pagar en\",\"viewOtherText\":\"Ver más productos de esta colección\",\"selectBicycleServiceCentreText\":\"Lo llevaré a un Servicio Técnico (sin costo)\",\"extendedWarrantyHeaderText\":\"Garantía Extendida\",\"lowStockText\":\"Últimas unidades\",\"deliveryCollectText\":\"Métodos de despacho\",\"addToBasketText\":\"Agregar a la bolsa\",\"extendedWarrantyReasonOne\":\"Protección más allá de la garantía del fabricante.\",\"normalPriceText\":\"Precio normal\",\"sizeText\":\"Tamaño\",\"electronicText\":\"Entrega Inmediata\",\"deliveryOptionsText\":\"Opciones de despacho\",\"nextImageText\":\"Siguiente\",\"selectWarrantyNoThanksText\":\"No, gracias\",\"fitGuideText\":\"Tabla de tallas\",\"totalMainText\":\"Valor cuota\",\"selectBicycleServiceTitleText\":\"¿Cómo quieres recibir tu bicicleta?\",\"whyExtendWarrantyText\":\"¿Por qué extender la garantía de tus productos?\",\"fulfilledByText\":\" y enviado por \",\"hideProductsText\":\"Ocultar productos\",\"selectBicycleServiceTooltipText\":\"Información sobre la opción de Armado de Bicicletas.\",\"cityText\":\"Ciudad\",\"itemQuantityText\":\"({0} productos)\",\"extendedWarrantyLinkText\":\"Ver más beneficios\",\"selectWarrantyTitleText\":\"Garantía Extendida\",\"regionText\":\"Región:\",\"monthlyInstallmentLabelText\":\"Cuotas mensuales\",\"selectComunaText\":\"Comuna\",\"clickAndCollectTabPriceText\":\"GRATIS\",\"acumulaPuntosText\":\"Acumula\",\"seeCollectionText\":\"Ver Colección\",\"deliveryOrderText\":\"Compra en los próximos {0} {1} para despacho en 24 horas.\",\"addItemToBasketText\":\"Agregar al Basket\",\"previousImageText\":\"Anterior\",\"financeCalculatorLabelText\":\"Calculadora financiera\",\"addToBasketTextLoading\":\"Agregando...\",\"soldByText\":\"Vendido por\",\"marketPlaceTooltipText\":\"Este producto es vendido por un partner de Falabella quien almacena sus productos en nuestros centros de distribución. Disfruta todos los beneficios de comprar en Falabella.com: Despacho a domicilio o retiro en tienda, devoluciones fáciles y nuestro servicio al cliente. \\n Para vender en nuestro marketplace entra a www.fmarketplace.com\",\"sizeGuideText\":\"Tabla de tallas\",\"collectText\":\"Retiro en Tienda\",\"oneClickText\":\"Compra en 1 click\",\"doneButtonText\":\"CONTINUAR\",\"insuredProductText\":\"\",\"productCodeText\":\"Código del producto:\",\"outOfStockText\":\"No disponible\",\"fitGuideModalTitleText\":\"Tabla de tallas\",\"pleaseChooseText\":\"Por favor selecciona\",\"editLocationText\":\"Editar\",\"homeDeliveryTabPriceText\":\"desde $3.990\",\"selectWarrantyDefaultSelectionText\":\"Sin protección adicional\",\"productNotAvailable\":\"Este producto ya no se encuentra disponible. Intenta seleccionando otra talla, tamaño o color.\",\"guideText\":\"Guía\",\"selectFurnitureServiceTitleText\":\"Servicio de armado de Muebles\",\"allImagesText\":\"Todas las imágenes\",\"writeAReviewText\":\"Crear comentario\",\"selectFurnitureServiceErrorText\":\"Por favor selecciona una opción\",\"selectWarrantyErrorText\":\"Por favor selecciona una opción\",\"deliveryCollectDetailsText\":\"colección desde {0}\",\"selectWarrantyTermsTitleText\":\"Términos y condiciones\",\"internetPriceText\":\"(Oferta)\",\"otherDimensionText\":\"Otra faceta\",\"yourLocationText\":\"Tu ubicación\",\"selectRegionText\":\"Región\",\"installmentsText\":\"Calcula tu cuota CMR\",\"isMoreText\":\"*+ {0} más\",\"bicycleAssemblyTermsTitleText\":\"Términos y condiciones\",\"collectPointsText\":\"Acumula\",\"viewMapText\":\"Ver mapa\",\"viewDeliveryOptionsText\":\"Ver Opciones de Despacho\",\"clickCollectText\":\"Retira tu orden\",\"selectCityText\":\"Ciudad\",\"totalReviewText\":\"\",\"comunaText\":\"Comuna:\",\"deliveryText\":\"Despacho a domicilio.\",\"selectFurnitureServiceTooltipText\":\"El costo del servicio contempla la complejidad del armado de cada producto.\",\"subTotalText\":\"Sub-total\",\"cae\":\"{1} cuotas de {0}. CAE {2}.\",\"colorText\":\"Color\",\"productNotAvailableText\":\"Producto no disponible\",\"electronicDownload\":\"(Descargable)\",\"collectionText\":\"Retira tu orden\",\"fitGuideTitleText\":\"Tabla de tallas\",\"selectWarrantyTooltipText\":\"Este servicio te permite extender la garantía de tu producto, brindándole cobertura total por costos de reparación, técnicos especializados y el reemplazo por uno nuevo de similares o superiores características, en caso de falla.\",\"itemsText\":\"productos\",\"selectBicycleServiceNoThanksText\":\"No, gracias\",\"promotionLinkText\":\"Ver promociones asociadas\",\"fromText\":\"desde\",\"totalInterestText\":\"CAE\",\"selectFurnitureServiceNoThanksText\":\"No, gracias\",\"selectBicycleServiceLocationText\":\"Ver ubicación de los servicios técnicos\",\"chooseOptionsText\":\"Elige tus opciones\",\"inStockText\":\"Disponible\",\"cuotasText\":\"cuotas\",\"whyExtendGuaranteeText\":\"\",\"sizeGuideModalTitleText\":\"Tabla de tallas\",\"CAEText\":\"CAE\"},\"endPoints\":{\"getProductShipping\":{\"name\":\"getProductShipping\",\"type\":\"GET\",\"path\":\"/rest/model/falabella/rest/browse/BrowseActor/get-product-shipping\"},\"getMonthlyInstallments\":{\"name\":\"getMonthlyInstallments\",\"type\":\"GET\",\"path\":\"/rest/model/falabella/rest/browse/BrowseActor/monthly-installment\"},\"initProductShipping\":{\"name\":\"initProductShipping\",\"type\":\"GET\",\"path\":\"/rest/model/falabella/rest/browse/BrowseActor/init-product-shipping\"},\"initMonthlyInstallments\":{\"name\":\"initMonthlyInstallments\",\"type\":\"GET\",\"path\":\"/rest/model/falabella/rest/browse/BrowseActor/init-monthly-installment\"},\"mediaAssetUrl\":{\"name\":\"mediaAssetUrl\",\"type\":\"POST\",\"path\":\"//falabella.scene7.com/is/image/\"},\"warrantyTermsLink\":{\"name\":\"warrantyTermsLink\",\"type\":\"GET\",\"path\":\"/static/site/content/infoComplementaria/descargableProductos/gar_ext_inf.pdf\"},\"bicycleServiceLink\":{\"name\":\"bicycleServiceLink\",\"type\":\"GET\",\"path\":\"/static/site/content/infoComplementaria/descargableProductos/gar_ext_inf.pdf\"},\"extendedWarrantyLink\":{\"name\":\"extendedWarrantyLink\",\"type\":\"GET\",\"path\":\"/falabella-cl/page/garantia?staticPageId=11300003\"}},\"componentType\":\"Browse-Main_Product\",\"state\":{\"product\":{\"hasPromotion\":false,\"isMarketPlace\":false,\"fulFilledBy\":\"falabella\",\"id\":\"5846832\",\"displayName\":\"LED 49\\\" 49MU6500 4K Ultra HD Smart TV Curvo\",\"backendCategory\":\"J11010305\",\"isCODAvailable\":false,\"useImageAtProductLevel\":false,\"mediaAssetId\":\"5846832\",\"brand\":\"Samsung\",\"published\":true,\"productType\":3,\"prices\":[{\"originalPrice\":\"339.990\",\"symbol\":\"$ \",\"type\":1,\"isLoyalty\":false},{\"label\":\"\",\"originalPrice\":\"579.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false}],\"socialLinks\":{\"hasFacebookShare\":true,\"hasGooglePlusShare\":true,\"hasEmailShare\":true,\"hasPinterestShare\":true,\"hasWhatsAppShare\":true,\"hasTwitterShare\":true,\"facebookShare\":{\"uri\":\"http://www.facebook.com/sharer.php?u=https://www.falabella.com/falabella-cl/product/5846832/LED-49\\\"-49MU6500-4K-Ultra-HD-Smart-TV-Curvo\",\"url\":\"https://www.falabella.com/falabella-cl/product/5846832/LED-49\\\"-49MU6500-4K-Ultra-HD-Smart-TV-Curvo\",\"message\":\"LED 49\\\" 49MU6500 4K Ultra HD Smart TV Curvo\"},\"pinterestShare\":{\"uri\":\"http://www.pinterest.com/pin/create/button/?url=https://www.falabella.com/falabella-cl/product/5846832/LED-49\\\"-49MU6500-4K-Ultra-HD-Smart-TV-Curvo&media=//falabella.scene7.com/is/image/FalabellaCL/5846832%3F%24nuPDP400%24&description=Me+gust%C3%B3+este+producto+Samsung+LED+49%22+49MU6500+4K+Ultra+HD+Smart+TV+Curvo.+%C2%A1Lo+quiero%21\",\"url\":\"https://www.falabella.com/falabella-cl/product/5846832/LED-49\\\"-49MU6500-4K-Ultra-HD-Smart-TV-Curvo\",\"message\":\"Me+gust%C3%B3+este+producto+Samsung+LED+49%22+49MU6500+4K+Ultra+HD+Smart+TV+Curvo.+%C2%A1Lo+quiero%21\",\"mediaUri\":\"//falabella.scene7.com/is/image/FalabellaCL/5846832%3F%24nuPDP400%24\"},\"twitterShare\":{\"uri\":\"http://www.twitter.com/share?text=Me gust%C3%B3 este producto Samsung LED+49%22+49MU6500+4K+Ultra+HD+Smart+TV+Curvo. Encu%C3%A9ntralo en&url=https://www.falabella.com/falabella-cl/product/5846832/\",\"url\":\"https://www.falabella.com/falabella-cl/product/5846832/LED-49\\\"-49MU6500-4K-Ultra-HD-Smart-TV-Curvo\",\"message\":\"Me gust%C3%B3 este producto Samsung LED+49%22+49MU6500+4K+Ultra+HD+Smart+TV+Curvo. Encu%C3%A9ntralo en\"},\"whatsAppShare\":{\"uri\":\"whatsapp://send?text=Me gustó este producto Samsung LED 49\\\" 49MU6500 4K Ultra HD Smart TV Curvo. ¡Lo quiero! https://www.falabella.com/falabella-cl/product/5846832/LED-49\\\"-49MU6500-4K-Ultra-HD-Smart-TV-Curvo\",\"url\":\"https://www.falabella.com/falabella-cl/product/5846832/LED-49\\\"-49MU6500-4K-Ultra-HD-Smart-TV-Curvo\",\"message\":\"Me gustó este producto Samsung LED 49\\\" 49MU6500 4K Ultra HD Smart TV Curvo. ¡Lo quiero! https://www.falabella.com/falabella-cl/product/5846832/LED-49\\\"-49MU6500-4K-Ultra-HD-Smart-TV-Curvo\"},\"googlePlusShare\":{\"uri\":\"https://plus.google.com/share?url=https://www.falabella.com/falabella-cl/product/5846832/LED-49\\\"-49MU6500-4K-Ultra-HD-Smart-TV-Curvo\",\"url\":\"https://www.falabella.com/falabella-cl/product/5846832/LED-49\\\"-49MU6500-4K-Ultra-HD-Smart-TV-Curvo\"},\"emailShare\":{\"template\":\"mailto:?subject=Me gustó este producto Samsung LED 49\\\" 49MU6500 4K Ultra HD Smart TV Curvo. ¡Lo quiero!&body=https://www.falabella.com/falabella-cl/product/5846832/LED-49\\\"-49MU6500-4K-Ultra-HD-Smart-TV-Curvo\",\"subject\":\"Me gustó este producto Samsung LED 49\\\" 49MU6500 4K Ultra HD Smart TV Curvo. ¡Lo quiero!\",\"body\":\"https://www.falabella.com/falabella-cl/product/5846832/LED-49\\\"-49MU6500-4K-Ultra-HD-Smart-TV-Curvo\"},\"iLikeThisProductText\":\"Me gusta este producto\",\"iWantItText\":\"Lo quiero\"},\"rating\":4.5,\"totalReviews\":25,\"meatStickers\":{\"second\":{\"title\":\"Exclusivo\",\"className\":\"exclusive\"}},\"relatedProducts\":[{\"id\":\"6034934\",\"displayName\":\"Chromecast 2 Generación + Cable HDMI\",\"brand\":\"Google\",\"url\":\"/falabella-cl/product/6034934/Chromecast-2-Generacion-%2B-Cable-HDMI/6034934\",\"useImageAtProductLevel\":false,\"mediaAssetId\":\"6034934\",\"prices\":[{\"label\":\"(Oferta)\",\"originalPrice\":\"39.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false},{\"label\":\"\",\"originalPrice\":\"59.990\",\"symbol\":\"$ \",\"type\":2,\"isLoyalty\":false}],\"skus\":[{\"skuId\":\"6034934\",\"name\":\"@CHROMECAST 2 GEN + CABLE HDMI\",\"mediaAssetId\":\"6034934\",\"price\":[{\"label\":\"(Oferta)\",\"originalPrice\":\"39.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false},{\"label\":\"\",\"originalPrice\":\"59.990\",\"symbol\":\"$ \",\"type\":2,\"isLoyalty\":false},{\"label\":\"CMR Puntos\",\"originalPrice\":\"266\",\"type\":14,\"isLoyalty\":false}],\"onlineStock\":306,\"lowStock\":false,\"stockAvailable\":306,\"isHDAvailable\":true,\"isCCAvailable\":false,\"isHDDisplay\":true,\"isCCDisplay\":true,\"isElectronicAvailable\":false}]},{\"id\":\"880630136\",\"displayName\":\"Soporte de TV 32\\\" - 55\\\" DD-MV102M\",\"brand\":\"Ddesign\",\"url\":\"/falabella-cl/product/880630136/Soporte-de-TV-32-55-DD-MV102M/880630136\",\"useImageAtProductLevel\":true,\"mediaAssetId\":\"880630136\",\"prices\":[{\"originalPrice\":\"5.990\",\"symbol\":\"$ \",\"type\":1,\"isLoyalty\":false},{\"label\":\"\",\"originalPrice\":\"12.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false}],\"skus\":[{\"skuId\":\"880630136\",\"name\":\"SOPORTES DD-MV102M\",\"mediaAssetId\":\"880630136\",\"price\":[{\"originalPrice\":\"5.990\",\"symbol\":\"$ \",\"type\":1,\"isLoyalty\":false},{\"label\":\"\",\"originalPrice\":\"12.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false},{\"label\":\"CMR Puntos\",\"originalPrice\":\"39\",\"type\":14,\"isLoyalty\":false}],\"onlineStock\":959,\"lowStock\":false,\"stockAvailable\":959,\"isHDAvailable\":true,\"isCCAvailable\":true,\"isHDDisplay\":true,\"isCCDisplay\":true,\"isElectronicAvailable\":false}]},{\"id\":\"3651417\",\"displayName\":\"Teclado Inalámbrico K400\",\"brand\":\"Logitech\",\"url\":\"/falabella-cl/product/3651417/Teclado-Inalambrico-K400/3651417\",\"useImageAtProductLevel\":true,\"mediaAssetId\":\"3651417\",\"prices\":[{\"label\":\"(Oferta)\",\"originalPrice\":\"19.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false},{\"label\":\"\",\"originalPrice\":\"24.990\",\"symbol\":\"$ \",\"type\":2,\"isLoyalty\":false}],\"skus\":[{\"skuId\":\"3651417\",\"name\":\"TECLADO  LOGITE  WIRELESS TOUCH K4\",\"mediaAssetId\":\"3651417\",\"price\":[{\"label\":\"(Oferta)\",\"originalPrice\":\"19.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false},{\"label\":\"\",\"originalPrice\":\"24.990\",\"symbol\":\"$ \",\"type\":2,\"isLoyalty\":false},{\"label\":\"CMR Puntos\",\"originalPrice\":\"133\",\"type\":14,\"isLoyalty\":false}],\"onlineStock\":2717,\"lowStock\":false,\"stockAvailable\":2717,\"isHDAvailable\":true,\"isCCAvailable\":true,\"isHDDisplay\":true,\"isCCDisplay\":true,\"isElectronicAvailable\":false}]},{\"id\":\"5382968\",\"displayName\":\"Google Chromecast Ultra\",\"brand\":\"Google\",\"url\":\"/falabella-cl/product/5382968/Google-Chromecast-Ultra/5382968\",\"useImageAtProductLevel\":false,\"mediaAssetId\":\"5382968\",\"prices\":[{\"label\":\"(Oferta)\",\"originalPrice\":\"69.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false},{\"label\":\"\",\"originalPrice\":\"129.990\",\"symbol\":\"$ \",\"type\":2,\"isLoyalty\":false}],\"skus\":[{\"skuId\":\"5382968\",\"name\":\"GOOGLE CHROMECAST ULTRA\",\"mediaAssetId\":\"5382968\",\"price\":[{\"label\":\"(Oferta)\",\"originalPrice\":\"69.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false},{\"label\":\"\",\"originalPrice\":\"129.990\",\"symbol\":\"$ \",\"type\":2,\"isLoyalty\":false},{\"label\":\"CMR Puntos\",\"originalPrice\":\"466\",\"type\":14,\"isLoyalty\":false}],\"onlineStock\":468,\"lowStock\":false,\"stockAvailable\":468,\"isHDAvailable\":true,\"isCCAvailable\":true,\"isHDDisplay\":true,\"isCCDisplay\":true,\"isElectronicAvailable\":false}]},{\"id\":\"5865970\",\"displayName\":\"Consola Android\",\"brand\":\"Joydi TV\",\"url\":\"/falabella-cl/product/5865970/Consola-Android/5865970\",\"useImageAtProductLevel\":false,\"mediaAssetId\":\"5865970\",\"prices\":[{\"label\":\"\",\"originalPrice\":\"69.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false}],\"skus\":[{\"skuId\":\"5865970\",\"name\":\"DISPOSITIVO SMART JOYDI TV PRO\",\"mediaAssetId\":\"5865970\",\"price\":[{\"label\":\"\",\"originalPrice\":\"69.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false},{\"label\":\"CMR Puntos\",\"originalPrice\":\"466\",\"type\":14,\"isLoyalty\":false}],\"onlineStock\":20,\"lowStock\":false,\"stockAvailable\":20,\"isHDAvailable\":false,\"isCCAvailable\":true,\"isHDDisplay\":true,\"isCCDisplay\":true,\"isElectronicAvailable\":false}]},{\"id\":\"5122056\",\"displayName\":\"Soporte Inclinable 37\\\" - 100\\\"\",\"brand\":\"Philips\",\"url\":\"/falabella-cl/product/5122056/Soporte-Inclinable-37-100-/5122056\",\"useImageAtProductLevel\":false,\"mediaAssetId\":\"5122056\",\"prices\":[{\"originalPrice\":\"19.990\",\"symbol\":\"$ \",\"type\":1,\"isLoyalty\":false},{\"label\":\"\",\"originalPrice\":\"24.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false}],\"skus\":[{\"skuId\":\"5122056\",\"name\":\"SOPORTE INCLINABLE 37-100 PULG.\",\"mediaAssetId\":\"5122056\",\"price\":[{\"originalPrice\":\"19.990\",\"symbol\":\"$ \",\"type\":1,\"isLoyalty\":false},{\"label\":\"\",\"originalPrice\":\"24.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false},{\"label\":\"CMR Puntos\",\"originalPrice\":\"133\",\"type\":14,\"isLoyalty\":false}],\"onlineStock\":270,\"lowStock\":false,\"stockAvailable\":270,\"isHDAvailable\":false,\"isCCAvailable\":true,\"isHDDisplay\":true,\"isCCDisplay\":true,\"isElectronicAvailable\":false}]},{\"id\":\"4777964\",\"displayName\":\"Apple TV 32GB  4ta Generación MGY52CI/A\",\"brand\":\"Apple\",\"url\":\"/falabella-cl/product/4777964/Apple-TV-32GB-4ta-Generacion-MGY52CI-A/4777964\",\"useImageAtProductLevel\":false,\"mediaAssetId\":\"4777964\",\"prices\":[{\"label\":\"\",\"originalPrice\":\"159.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false}],\"skus\":[{\"skuId\":\"4777964\",\"name\":\"APPLE TV 32GB-CHL  MGY52CI/A\",\"mediaAssetId\":\"4777964\",\"price\":[{\"label\":\"\",\"originalPrice\":\"159.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false},{\"label\":\"CMR Puntos\",\"originalPrice\":\"1.066\",\"type\":14,\"isLoyalty\":false}],\"onlineStock\":19,\"lowStock\":false,\"stockAvailable\":19,\"isHDAvailable\":false,\"isCCAvailable\":true,\"isHDDisplay\":true,\"isCCDisplay\":true,\"isElectronicAvailable\":false}]},{\"id\":\"5438464\",\"displayName\":\"Kit Deco Satelital Antena 60c\",\"brand\":\"Magic Tv\",\"url\":\"/falabella-cl/product/5438464/Kit-Deco-Satelital-Antena-60c/5438464\",\"useImageAtProductLevel\":false,\"mediaAssetId\":\"5438464\",\"prices\":[{\"label\":\"\",\"originalPrice\":\"85.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false}],\"skus\":[{\"skuId\":\"5438464\",\"name\":\"@KIT DECO SATELIT DH cANTENA 60c\",\"mediaAssetId\":\"5438464\",\"price\":[{\"label\":\"\",\"originalPrice\":\"85.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false},{\"label\":\"CMR Puntos\",\"originalPrice\":\"573\",\"type\":14,\"isLoyalty\":false}],\"onlineStock\":2,\"lowStock\":false,\"stockAvailable\":2,\"isHDAvailable\":true,\"isCCAvailable\":true,\"isHDDisplay\":true,\"isCCDisplay\":true,\"isElectronicAvailable\":false}]}],\"warranty\":[{\"id\":\"2577462\",\"price\":{\"originalPrice\":\"39.990\",\"symbol\":\"$ \",\"isLoyalty\":false},\"option\":{\"label\":\"1 Año\",\"value\":\"2577462\",\"selected\":false}},{\"id\":\"2577468\",\"price\":{\"originalPrice\":\"69.990\",\"symbol\":\"$ \",\"isLoyalty\":false},\"option\":{\"label\":\"2 Años\",\"value\":\"2577468\",\"selected\":false}},{\"id\":\"2577474\",\"price\":{\"originalPrice\":\"89.990\",\"symbol\":\"$ \",\"isLoyalty\":false},\"option\":{\"label\":\"3 Años\",\"value\":\"2577474\",\"selected\":false}}],\"skus\":[{\"skuId\":\"5846832\",\"name\":\"LED SAMSUNG UN49MU6500\",\"mediaAssetId\":\"5846832\",\"meatStickers\":{\"second\":{\"title\":\"Exclusivo\",\"className\":\"exclusive\"}},\"price\":[{\"originalPrice\":\"339.990\",\"symbol\":\"$ \",\"type\":1,\"isLoyalty\":false},{\"label\":\"\",\"originalPrice\":\"579.990\",\"symbol\":\"$ \",\"type\":3,\"isLoyalty\":false},{\"label\":\"CMR Puntos\",\"originalPrice\":\"2.266\",\"type\":14,\"isLoyalty\":false}],\"onlineStock\":602,\"lowStock\":false,\"stockAvailable\":602,\"isHDAvailable\":true,\"isCCAvailable\":true,\"isHDDisplay\":true,\"isCCDisplay\":true,\"isElectronicAvailable\":false}]}}}";
//            log.info("json->"+pa[1].trim());
//            String[] pJson = pa[1].trim().split("");
//            for(String p1 : pJson){
//                log.info("p1->"+p1);
//            }
//            ObjectMapper mapper = new ObjectMapper();
//            Object someObject  = mapper.readValue(json, Object.class);
//            Object state = getValueFromObject(someObject, "state");
//            if(state!=null){
//                log.info("state encontrado...");
//                Object product = getValueFromObject(someObject, "product");
//                if(product!=null){
//                        log.info("product encontrado...");
//                }
//                
//            }
//            for (Field field : someObject.getClass().getDeclaredFields()) {
//                field.setAccessible(true); // You might want to set modifier to public first.
//                Object value; 
//                try {
//                    value = field.get(someObject);
//                    if (value != null) {
//                        System.out.println(field.getName() + "------>" + value);
//                    }
//                } catch (IllegalArgumentException ex) {
//                    java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IllegalAccessException ex) {
//                    java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            }
//            for (Field field : someObject.getClass().getDeclaredFields()) {
//                field.setAccessible(true); // You might want to set modifier to public first.
//                try {
//                    Object value = field.get(someObject);
//                    System.out.println("field.getName()->"+field.getName());
//                    
//                    for(Field field2 : value.getClass().getDeclaredFields()){
//                        field2.setAccessible(true); 
//                        System.out.println("field2.getName()->"+field2.getName());
//                    }
//
//
//                    
//                } catch (IllegalArgumentException ex) {
//                    java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IllegalAccessException ex) {
//                    java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//            }
//log.info("-->"+someObject .toString());
//            for (Field field : someObject.getClass().getDeclaredFields()) {
//                field.setAccessible(true); // You might want to set modifier to public first.
//                Object value;
//                
//                try {
//                    value = field.get(someObject);
//                    value.getClass().get;
//
//
//
//
//                    
//                    System.out.println("field.getName()->"+field.getName());
//                    if (value != null && field.getName().equals("state")) {
//                        System.out.println("es state...");
//                        try {
//                            Object product = value.getClass().getDeclaredField("product");
//                            value = field.get(product);
//                            System.out.println("product=" + value);
//                        } catch (NoSuchFieldException ex) {
//                            java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (SecurityException ex) {
//                            java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        
//                    }
//                } catch (IllegalArgumentException ex) {
//                    java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IllegalAccessException ex) {
//                    java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//            }


//            Elements scripts = document.select("script");
//            for (Element page : scripts) {                                
//              log.info(page.text());
//            }


//Elements precioOferta = document.select("div.fb-product-cta__container");
// Elements precioOferta = document.select("div.fb-product__form");
//log.info("html:"+precioOferta.html());
//            log.info("-->"+precioOferta.eq(0).text());
//            for (Element page : precioOferta) {                                
//              log.info(page.text() +"-"+page.className());
//            }
//log.info(tituloProducto.get(0).text());

//Elements pngs = doc.select("img[src$=.png]");



//           Document document2 = Jsoup.connect("https://www.paris.cl/store/producto/macbook-air-intel-core-i5-8gb-ram-128gb-13-3-237653-PPP-?cat_tecnologia=Tecno&cat_tecnologia-computadores=Computadores&cat_tecnologia-computadores-notebook=Notebook").get();
//            
//           Element  aaa = document2.getElementById("p.offerPrice");
//            log.info("htmlsss:"+aaa.html());
            } catch (ParseException ex) {
                java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public Object getValueFromObject(Object obj,String property){
        Class<?> clazz = obj.getClass();
        Field field;
        Object fieldValue = null;
        try {
            field = clazz.getField(property);
            if(field!=null){
                field.setAccessible(true);
                fieldValue = field.get(obj);
            }
            
        }catch (IllegalArgumentException ex) {
                java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (NoSuchFieldException ex) {
            java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fieldValue;
    }
    public void getHtmlViewAfterJS(){
        
    }
    public void downloadPage(){
        try {
            WebClient webClient = null;
            String START_URL = "https://www.paris.cl/store/producto/juego-de-comedor-julieta-4-sillas-sarah-miller-235926-PPP-?cat_muebles=Muebles&cat_muebles-comedor=Comedor%20&cat_muebles-comedor-juegos-comedor=Juegos%20de%20Comedor";
//        try {
//            log.info("conectando por chrome...");
//            webClient = new WebClient(BrowserVersion.FIREFOX_2);
//            HtmlPage page = webClient.getPage(START_URL);
//            webClient. .getOptions().setJavaScriptEnabled(true);
//            webClient.waitForBackgroundJavaScript(3000);
//            log.info("page->{}",page.asText());
//            log.info("fin...");
//            
//        } catch (IOException ex ) {
//            ex.printStackTrace();
//        }
            //http://htmlunit.sourceforge.net/gettingStarted.html
            //https://www.reddit.com/r/learnprogramming/comments/2rtuvv/need_help_extracting_a_string_from_a_div_using/
            log.info("conectando por FIREFOX_3...");
            //log.info("div.getTextContent2()->{}",div.getTextContent());
            webClient = new WebClient(BrowserVersion.FIREFOX_3);
            HtmlPage page = webClient.getPage(START_URL);
            final HtmlDivision div = page.getHtmlElementById("product");
            webClient.waitForBackgroundJavaScript(10000);
            
            log.info("div.getTextContent3()->{}",div.getTextContent());
//            webClient.setJavaScriptEnabled(false);
//            webClient.setCssEnabled(false);
//            webClient.waitForBackgroundJavaScript(10000);
//            log.info("page->{}",page.asText());
            
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FailingHttpStatusCodeException ex) {
            java.util.logging.Logger.getLogger(CrawlerServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
