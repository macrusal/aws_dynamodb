package br.com.hibejix.amazonaws.dynamodb.serverless.handler.produtos;

import br.com.hibejix.amazonaws.dynamodb.serverless.Response;
import br.com.hibejix.amazonaws.dynamodb.entity.Produtos;
import br.com.hibejix.amazonaws.dynamodb.serverless.ApiGatewayResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class CreateProdutosHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());


    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> stringObjectMap, Context context) {

        try {
            JsonNode body = new ObjectMapper().readTree((String) stringObjectMap.get("body"));
            Produtos produtos = new Produtos();
            produtos.setNome(body.get("nome").asText());
            produtos.setPrecoCompra(body.get("PrecoCompra").asText());
            produtos.setPrecoVenda(body.get("PrecoVenda").asText());
            produtos.setPrecoSugerido(body.get("PrecoSugerido").asText());
            produtos.save(produtos);

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(produtos)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        } catch (Exception e) {
            logger.error("Erro ao salvar produto: " + e);

            Response responsebody = new Response("Erro ao salvar produto: ", stringObjectMap);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responsebody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        }
    }
}
