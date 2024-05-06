package bantunes82.ai.langchainspringboot.controller;

import bantunes82.ai.langchainspringboot.dto.MyQuestion;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel.OllamaChatModelBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

/**
 *      For info about ollama (https://ollama.com)
 *             - to install ollama 3 in Linux: curl -fsSL https://ollama.com/install.sh | sh
 *             - to run locally: ollama run llama3
 */
@RestController
public class OllamaControler {

    @PostMapping("/chatollama")
    public String chatWithOllama(@RequestBody MyQuestion question){
        ChatLanguageModel ollamaChat = new OllamaChatModelBuilder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3")
                .timeout(Duration.ofSeconds(300))
                .build();

        return ollamaChat.generate(question.question());
    }
}
