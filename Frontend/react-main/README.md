# Projeto React (Vite)

Breve descrição
- Aplicação frontend construída com React (Vite como bundler). Este repositório contém código-fonte em `src/` e arquivos estáticos em `public/`.

Requisitos
- Node.js (recomenda-se LTS: 18/20)
- npm

Instalação
1. Instale as dependências:

```bash
npm install
```

Scripts úteis (conforme `package.json`)
- `npm run dev` — inicia o servidor de desenvolvimento (Vite)
- `npm run build` — gera build de produção
- `npm run preview` — pré-visualiza o build de produção
- `npm run lint` — executa o ESLint

Como executar em desenvolvimento

```bash
npm install
npm run dev
```

Erro comum: TypeError [ERR_UNKNOWN_FILE_EXTENSION]: Unknown file extension ".jsx"

Causa
- Esse erro ocorre quando você tenta executar ficheiros de origem React (com JSX/TSX) diretamente com o `node`. O Node.js, por padrão, não transforma JSX. Em projetos React usamos um bundler (Vite) ou um transpiler para converter JSX em JavaScript válido antes de executar.

Soluções
1) Usar o script de desenvolvimento (recomendado)
- Em vez de executar `node src/App.jsx` use:

```bash
npm run dev
```

Isso inicia o Vite, que lida com JSX/TSX automaticamente.

2) Executar apenas código Node puro
- Se precisar executar um script Node sem transpilar JSX, remova JSX ou execute um arquivo que não contenha JSX (por exemplo um script em `scripts/` sem React).

3) Usar um runner que transpila na hora (ex.: `tsx`)
- Se quiser executar ficheiros com sintaxe avançada diretamente, instale e use `tsx`:

```bash
npm install -D tsx
npx tsx src/App.jsx
```

4) Converter para `.tsx` se estiver a usar TypeScript
- Para ficheiros React com TypeScript, renomeie para `.tsx` e configure `tsconfig.json` adequadamente.

Notas adicionais
- Não execute ficheiros fonte React diretamente com `node`.
- O projeto já tem Vite nas devDependencies; usar `npm run dev` é a forma correta de trabalhar.

Estrutura recomendada
- `src/` — código-fonte React
- `public/` — assets públicos
- `package.json` — scripts e dependências
- `tsconfig.json` — configuração TypeScript (se aplicável)

Se quiser, posso também adicionar instruções para instalar `@vitejs/plugin-react` e um exemplo de `vite.config.js` mínimo, ou converter `.jsx` para `.tsx` onde fizer sentido — diz-me o que preferes.
