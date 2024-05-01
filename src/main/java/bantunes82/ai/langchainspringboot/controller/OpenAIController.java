package bantunes82.ai.langchainspringboot.controller;

import bantunes82.ai.langchainspringboot.dto.MyQuestion;
import bantunes82.ai.langchainspringboot.dto.PromptRecipe;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel.OpenAiChatModelBuilder;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel.OpenAiImageModelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.List;

@RestController
public class OpenAIController {

    private final String apiKey;
    private final ChatLanguageModel chatModel;

    public OpenAIController(@Value("${OPENAI_KEY}") String apiKey, ChatLanguageModel chatModel) {
        this.apiKey = apiKey;
        this.chatModel = chatModel;
    }

    @PostMapping("/answer")
    public String chatWithOpenAI(@RequestBody MyQuestion question){
        OpenAiChatModel customModel = new OpenAiChatModelBuilder()
                                        .apiKey(apiKey)
                                        .modelName("gpt-3.5-turbo")
                                        .temperature(0.1)
                                        .build();

        return customModel.generate(question.question());
    }

    @GetMapping("/recipe")
    public String makeARecipe(){
        var promptRecipe = new PromptRecipe("Assado", List.of("carne", "tomate", "cebola", "pimentao"));

        Prompt prompt = StructuredPromptProcessor.toPrompt(promptRecipe);

        return chatModel.generate(prompt.text());
    }

    @PostMapping("/image")
    public String generateImage(@RequestBody MyQuestion question) throws MalformedURLException {
        OpenAiImageModel imageModel = new OpenAiImageModelBuilder()
                .apiKey(apiKey)
                .modelName("dall-e")
                .build();

        return imageModel.generate(question.question())
                .content().url().toURL().toString();
    }

}
