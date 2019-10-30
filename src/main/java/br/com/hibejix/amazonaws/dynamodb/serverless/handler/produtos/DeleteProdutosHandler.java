package br.com.hibejix.amazonaws.dynamodb.serverless.handler.produtos;

import br.com.hibejix.amazonaws.dynamodb.entity.Produtos;
import br.com.hibejix.amazonaws.dynamodb.serverless.ApiGatewayResponse;
import br.com.hibejix.amazonaws.dynamodb.serverless.Response;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class DeleteProdutosHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> stringObjectMap, Context context) {

        try {

            Map<String, String> pathParameters = (Map<String, String>) stringObjectMap.get("pathParameters");
            String produtoUuid = pathParameters.get("uuid");

            Boolean success = new Produtos().delete(produtoUuid);

            if(success) {
                return ApiGatewayResponse.builder()
                        .setStatusCode(204)
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
            logger.error("Erro ao apagar produto: " + e);

            Response responsebody = new Response("Erro ao apagar produto: ", stringObjectMap);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responsebody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        }
    }
}
