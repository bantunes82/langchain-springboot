package bantunes82.ai.langchainspringboot.rag;

import ai.djl.util.Utils;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;


import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;

@Service
public class RAGConfiguration {

    private final String apiKey;

    public RAGConfiguration(@Value("${OPENAI_KEY}") String apiKey) {
        this.apiKey = apiKey;
    }

    public Assistant configure() throws Exception{
        //step 1 - get the documment List
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(toPath("documents/"), glob("*.txt"));

        //step 2 - create the assistent
        /**
         * - 1 chat model
         * - 2 chat memory
         * - 3 element to retrieval the content
         */
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(OpenAiChatModel.withApiKey(apiKey))
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(createContentRetriever(documents))
                .build();


        return assistant;
    }


    public ContentRetriever createContentRetriever(List<Document> documents) {
        //get all the documents and create an Embedded Store in memory
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        //Convert the tokens of the documents in numerics vectors
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);

        //create effectivelly the Content Retriever from Embedding Store
        return EmbeddingStoreContentRetriever.from(embeddingStore);
    }


    //Methods to load the documents

    public PathMatcher glob(String glob){
        return FileSystems.getDefault().getPathMatcher("glob:" + glob);
    }

    public Path toPath(String path){
        try {
            URL fileUrl = Utils.class.getClassLoader().getResource(path);
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
