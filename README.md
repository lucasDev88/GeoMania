# Geo Mania

Aplicativo educacional Android sobre **Recursos naturais: apropriação e usos**, baseado no documento de arquitetura desta pasta. Implementação nativa em Kotlin e Jetpack Compose, com conteúdo, GeoMatch, Escolhas da Terra e GeoExpedição offline.

## Entrega atual — 0.6.0

APK: `entrega/GeoMania-0.6.0-debug.apk`. Inclui Camo, o mascote camaleão, e GeoExpedição: mapa interativo, oito locais, três missões e 24 pistas. A Home apresenta os três jogos. Veja `docs/GEOEXPEDICAO.md` para regras e roteiro de demonstração. Instale como atualização sem desinstalar o app anterior.

Animações da versão 0.6.0: Camo interativo, cenário ambiente, cartões e telas em movimento, giro 3D no GeoMatch, indicadores animados e comemorações com partículas. Detalhes e conferência no aparelho em `docs/ANIMACOES.md`.

## Abrir e compilar

Abra esta pasta como projeto no Android Studio. Use JDK 17 ou 21, SDK Android 35 e Build Tools 35.0.0. O Gradle Wrapper está incluído. O primeiro build precisa de internet para as dependências; o aplicativo instalado funciona offline.

No PowerShell, com `JAVA_HOME` configurado e `local.properties` apontando para o SDK:

```powershell
.\gradlew.bat testDebugUnitTest assembleDebug lintDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`. É uma versão de desenvolvimento para testes e apresentação, não uma publicação em loja.

Cópia pronta para transferir: `entrega/GeoMania-debug.apk`. O usuário confirmou em 28/09/2026 que a versão 0.1.0 funcionou no celular. A versão 0.2.0 acrescentou Escolhas da Terra; os resultados por versão e as conferências manuais estão em `docs/VALIDACAO.md`.

A versão 0.5.0 incorpora o mascote ao ícone adaptável, apresenta navegação por abas e aprimora contraste, leitura por voz e andamento das decisões. A 0.4.0 introduziu o Camo e a GeoExpedição com oito locais e três missões. O mascote está em `app/src/main/res/drawable-nodpi/camo_mascot.png`. Confira `docs/GEOEXPEDICAO.md` e `docs/VALIDACAO.md`.

A versão 0.3.0 redesenha a Home com paisagem vetorial, atalhos para os jogos e painel de recorde. A linha do tempo apresenta etapas conectadas, resumos dos sete contextos e detalhes com recursos em etiquetas, perguntas de reflexão e navegação anterior/próxima. APK específico: `entrega/GeoMania-0.3.0-debug.apk`.

Nesta máquina, foi preparado um SDK local em `.tools/android-sdk`. As ferramentas e caches ficam fora do código versionável. O Java encontrado é `C:/Users/Lucas/.jdks/ms-21.0.10`; configure `JAVA_HOME` para ele se o terminal não localizar Java.

## Conteúdo e arquitetura

- `app/src/main/assets`: sete contextos históricos, seis recursos, seis pares do GeoMatch, referências e textos didáticos. Edite estes JSONs para revisar o conteúdo.
- `app/src/main/java/br/com/geomania/data`: modelos, repositório local e DataStore.
- `app/src/main/java/br/com/geomania/game`: regras puras e estado da partida no ViewModel.
- `app/src/main/java/br/com/geomania/ui`: navegação e telas Compose.
- `app/src/test`: testes de regras do jogo.
- `docs/PLANO_3_DIAS.md`: plano original e atualização do prazo final para 09/10.
- `docs/VALIDACAO.md`: verificações realizadas e roteiro de testes manuais.
- `docs/DEMONSTRACAO.md`: apresentação de 3–5 minutos.
- `docs/INSTALAR_SEM_USB.md`: transferência e instalação manual no celular.

O temporizador começa no primeiro toque, pausa quando o app sai do primeiro plano e durante a leitura das explicações. A rotação mantém a partida via ViewModel. Sair da tela do jogo ou encerrar o processo descarta a partida incompleta; recordes e partidas concluídas ficam no DataStore. Não há cadastro, permissão de internet ou transmissão de dados pelo app.

## Escolhas da Terra

Após a linha do tempo, o novo modo apresenta quatro fases independentes e 16 decisões com três alternativas cada. Também há acesso direto pelo início. Os indicadores de natureza, produção, sociedade e recursos variam de 0 a 100. Efeitos, explicações, contexto e referências ficam em `app/src/main/assets/earth_choices.json`.

Os números são parâmetros de uma simulação didática, não estatísticas históricas nem pontuações para classificar sociedades. Cada fase reinicia os indicadores; seus resultados e decisões ficam na sessão para comparação. Não há alternativa sempre correta, derrota automática ou ranking. Reiniciar permite experimentar outras prioridades. A rotação mantém o estado; sair do modo ou encerrar o processo descarta a sessão do simulador.

Instale o APK como atualização, mantendo o aplicativo anterior, para preservar o recorde do GeoMatch. A versão usa o mesmo identificador e assinatura debug e incrementa o código de versão.

## Revisar os dados

```powershell
python scripts/validate_content.py
```

A validação usa apenas a biblioteca padrão do Python. O script de importação inicial `scripts/import_content.py` depende de `python-docx` e sobrescreve os JSONs: não o execute após revisões manuais. O documento original foi preservado.

As fontes temáticas e os recortes históricos devem passar pela revisão do grupo/professor antes da apresentação. A linha do tempo explicita que sociedades e formas de apropriação coexistem.

## Decisões de build

Versões fixadas: AGP 8.11.1, Gradle 8.13, Kotlin/Compose Compiler 2.1.20 e Compose BOM 2025.03.00. A compatibilidade de AGP e Gradle foi conferida na [documentação Android](https://developer.android.com/build/releases/agp-8-11-0-release-notes). O BOM coordena versões das bibliotecas Compose conforme a [documentação oficial](https://developer.android.com/develop/ui/compose/bom).
