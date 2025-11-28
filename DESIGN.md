# 🎨 Documentação do Design - Pet Shop

## Conceito Visual: Glassmorphism

O design do sistema utiliza o conceito moderno de **Glassmorphism** (morfismo de vidro), caracterizado por:

### Elementos Principais

#### 1. **Efeito de Vidro Fosco**
```css
background: rgba(255, 255, 255, 0.15);
backdrop-filter: blur(20px);
```
- Cards com transparência
- Efeito de desfoque no fundo
- Sensação de profundidade

#### 2. **Gradiente Animado de Fundo**
- Cores vibrantes que se movem suavemente
- Paleta: roxo, rosa, azul, ciano
- Animação de 15 segundos em loop
- Cria um ambiente dinâmico e moderno

#### 3. **Bordas e Sombras Sutis**
```css
border: 1px solid rgba(255, 255, 255, 0.3);
box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
```
- Bordas translúcidas
- Sombras que dão profundidade
- Cantos arredondados (border-radius: 20px)

### Paleta de Cores

#### Cores Primárias
- **Primary:** `#6366f1` (Azul Índigo)
- **Secondary:** `#ec4899` (Rosa)
- **Success:** `#10b981` (Verde)
- **Error:** `#ef4444` (Vermelho)
- **Warning:** `#f59e0b` (Laranja)

#### Cores de Texto
- **Primário:** `#1f2937` (Cinza Escuro)
- **Secundário:** `#6b7280` (Cinza Médio)
- **Light:** `#ffffff` (Branco)

### Componentes Visuais

#### 🐾 **Cards de Animais**
- Fundo branco translúcido (95% opacidade)
- Ícone emoji grande no topo
- Hover: elevação e sombra aumentada
- Tags coloridas para informações (vacinado, castrado, disponível)
- Transição suave em todas as interações

#### 📋 **Sistema de Abas**
- Abas horizontais com ícones
- Aba ativa: gradiente azul + sombra
- Abas inativas: transparência
- Transição suave ao trocar

#### 🔘 **Botões**
- **Primário:** Gradiente azul com sombra colorida
- **Secundário:** Fundo transparente com borda
- **Danger:** Gradiente vermelho
- **Success:** Gradiente verde
- Hover: elevação de 2px + sombra aumentada

#### 📝 **Formulários**
- Inputs com fundo branco semi-transparente
- Borda sutil que se intensifica no foco
- Labels em branco para contraste
- Validação visual

#### 🔔 **Alertas e Notificações**
- Fundo colorido translúcido
- Animação de slide-in
- Auto-dismiss após 4 segundos
- Tipos: success, error, warning

### Animações

#### 1. **FadeIn**
```css
@keyframes fadeIn {
    from { opacity: 0; transform: translateY(20px); }
    to { opacity: 1; transform: translateY(0); }
}
```
- Usada em: cards, modais, páginas

#### 2. **Bounce**
```css
@keyframes bounce {
    0%, 100% { transform: translateY(0); }
    50% { transform: translateY(-10px); }
}
```
- Usada em: logo, ícones

#### 3. **GradientShift**
```css
@keyframes gradientShift {
    0% { background-position: 0% 50%; }
    50% { background-position: 100% 50%; }
    100% { background-position: 0% 50%; }
}
```
- Usada em: background principal

#### 4. **SlideIn**
```css
@keyframes slideIn {
    from { opacity: 0; transform: translateX(-20px); }
    to { opacity: 1; transform: translateX(0); }
}
```
- Usada em: alertas, notificações

### Responsividade

#### Breakpoints
- **Desktop:** > 768px
- **Mobile:** ≤ 768px

#### Ajustes Mobile
- Header: flex-direction column
- Tabs: stack vertical
- Grid: 1 coluna
- Padding reduzido
- Fontes ajustadas

### Ícones

Uso de emojis para melhor experiência visual:
- 🐾 Pet Shop (logo)
- 🐕 Cachorro
- 🐈 Gato
- 🐦 Pássaro
- 🐰 Coelho
- ❤️ Adoção
- 📋 Formulários
- ✓ Sucesso
- ⚠ Aviso
- ❌ Erro

### Tipografia

#### Fonte
- **Family:** Segoe UI, Tahoma, Geneva, Verdana, sans-serif
- **Pesos:** 400 (normal), 500 (médio), 600 (semi-bold), 700 (bold)

#### Tamanhos
- **H1:** 2.5rem
- **H2:** 1.8rem
- **H3:** 1.3rem
- **Body:** 1rem
- **Small:** 0.9rem

#### Line-height
- **Padrão:** 1.6
- **Títulos:** 1.2

### Efeitos de Hover

Todos os elementos interativos possuem feedback visual:
- **Botões:** elevação + sombra
- **Cards:** elevação + sombra
- **Inputs:** borda colorida + sombra
- **Links:** sublinhado
- **Tabs:** background + elevação

### Scrollbar Personalizada

```css
::-webkit-scrollbar {
    width: 10px;
    background: rgba(255, 255, 255, 0.1);
}
::-webkit-scrollbar-thumb {
    background: rgba(255, 255, 255, 0.3);
    border-radius: 10px;
}
```

### Estados Vazios

Quando não há dados:
- Ícone grande (4rem)
- Título descritivo
- Mensagem de ajuda
- Cor suave

### Acessibilidade

- Contraste adequado entre texto e fundo
- Tamanhos de fonte legíveis
- Áreas de clique adequadas (min 44x44px)
- Estados de foco visíveis
- Textos alternativos

### Performance

- Transições otimizadas (transform + opacity)
- Hardware acceleration (backdrop-filter)
- Lazy loading de imagens (quando implementado)
- Animações pausadas quando fora da tela

### Consistência Visual

Todos os elementos seguem o mesmo padrão:
- Border-radius: 12px ou 20px
- Padding: múltiplos de 0.5rem
- Margin: múltiplos de 0.5rem
- Gaps em grid: 1rem ou 1.5rem

---

## 🎯 Resultado Final

Um design moderno, limpo e profissional que:
- ✅ É agradável aos olhos
- ✅ Facilita a navegação
- ✅ Transmite confiança
- ✅ É responsivo
- ✅ Tem ótima UX
- ✅ Segue tendências atuais
- ✅ Sem bugs visuais

**Tecnologias puras:** HTML5 + CSS3 + JavaScript Vanilla
**Sem frameworks CSS:** Design totalmente customizado
**Sem bugs:** Testado e validado
