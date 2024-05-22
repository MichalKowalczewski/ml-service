package com.example.mlservice.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType.BestFields;

@Component
public class ElasticsearchRepository<T> {

    private final ElasticsearchClient client;

    public ElasticsearchRepository(ElasticsearchClient client) {
        this.client = client;
    }

    public List<T> findAllDocuments(Class<T> returnDataType, String index) throws IOException {

        SearchRequest request = SearchRequest.of(requestBuilder ->  requestBuilder
                .index(index)
                .size(10000)
                .query(queryBuilder -> queryBuilder
                        .matchAll(builder -> builder))
        );

        return client.search(request, returnDataType)
                .hits().hits().stream().map(Hit::source)
                .collect(Collectors.toList());
    }

    public List<T> multiMatchSearch(Class<T> returnDataType,
                                    String index,
                                    long size,
                                    String query) throws IOException {

        SearchRequest request = SearchRequest.of(requestBuilder -> requestBuilder
                .query(queryBuilder -> queryBuilder
                        .multiMatch(multiMatchBuilder -> multiMatchBuilder
                                .type(BestFields)
                                .fuzziness("2")
                                .query(query))
                ));

        return client.search(request, returnDataType).hits().hits().stream().map(Hit::source)
                .collect(Collectors.toList());
    }

    public List<KnnResult<T>> findNearestNeighbors(Class<T> returnDataType,
                                         String index,
                                         long size,
                                         List<Float> vectorValue) throws IOException {

        SearchRequest request = SearchRequest.of( requestBuilder -> requestBuilder
                .knn(knnBuilder -> knnBuilder
                        .k(size)
                        .numCandidates(100L)
                        .queryVector(vectorValue)
                        .field("vector"))
                .index(index));

        return client.search(request, returnDataType)
                .hits().hits().stream().map(tHit -> new KnnResult<>(tHit.source(), tHit.score()))
                .collect(Collectors.toList());
    }

    public void insert(T value, String index) throws IOException {
        client.index(builder -> builder
                .document(value)
                .index(index));
        client.indices().refresh(builder -> builder.index(index));
    }
}
