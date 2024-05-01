package bantunes82.ai.langchainspringboot.dto;

import dev.langchain4j.model.input.structured.StructuredPrompt;

import java.util.List;

@StructuredPrompt({
        "Crie uma receita de {{plate}} que possa ser preparada usando somente {{ingredients}}",
        "Estruture a sua resposta da seguinte forma:",
        "Nome da Receita: ...",
        "Descricao: ...",
        "Tempo de Preparo: ...",
        "Ingredientes Necessarios:",
        "- ...",
        "- ...",
        "Modo de Preparo:",
        "- ...",
        "- ..."
})
public record PromptRecipe(String plate, List<String> ingredients) {
}
