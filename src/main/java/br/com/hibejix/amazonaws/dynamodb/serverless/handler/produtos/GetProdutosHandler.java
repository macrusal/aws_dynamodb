package br.com.hibejix.amazonaws.dynamodb.serverless.handler.produtos;

import br.com.hibejix.amazonaws.dynamodb.entity.Produtos;
import br.com.hibejix.amazonaws.dynamodb.serverless.ApiGatewayResponse;
import br.com.hibejix.amazonaws.dynamodb.serverless.Response;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class GetProdutosHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> stringObjectMap, Context context) {

        try {
            Map<String, String> pathParameters = (Map<String, String>) stringObjectMap.get("pathParameters");
            String produtoUuid = pathParameters.get("uuid");

            Produtos produtos = new Produtos().get(produtoUuid);

            if(produtos != null) {
                return ApiGatewayResponse.builder()
                        .setStatusCode(200)
                        .setObjectBody(produtos)
                        .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                        .build();
            } else {
                return ApiGatewayResponse.builder()
                        .setStatusCode(404)
                        .setObjectBody("Produto com uuid: '" + produtoUuid + "' não encontrado!!! ")
                        .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                        .build();
            }

        } catch (Exception e) {
            logger.error("Erro ao buscar produto: " + e);

            Response responsebody = new Response("Erro ao buscar produto: ", stringObjectMap);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responsebody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        }
    }
}
