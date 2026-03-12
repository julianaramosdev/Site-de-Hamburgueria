package com.example.hamburgueriaz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    
    // Declaração dos componentes da interface
    private EditText editTextNome;
    private TextView textViewQuantidade, textViewResumo, textViewPreco;
    private CheckBox checkBoxBacon, checkBoxQueijo, checkBoxOnionRings;
    private Button buttonMais, buttonMenos, buttonEnviar;
    private ImageView imageViewLogo;
    
    // Variáveis de controle
    private int quantidade = 0;
    private final int PRECO_BASE = 20;
    private final int PRECO_BACON = 2;
    private final int PRECO_QUEIJO = 2;
    private final int PRECO_ONION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Inicialização dos componentes
        initComponents();
        
        // Configuração dos listeners
        setupListeners();
        
        // Atualização inicial do preço
        atualizarPreco();
    }
    
    /**
     * Método para inicializar todos os componentes da interface
     */
    private void initComponents() {
        try {
            editTextNome = findViewById(R.id.editTextNome);
            textViewQuantidade = findViewById(R.id.textViewQuantidade);
            textViewResumo = findViewById(R.id.textViewResumo);
            textViewPreco = findViewById(R.id.textViewPreco);
            imageViewLogo = findViewById(R.id.imageViewLogo);
            
            checkBoxBacon = findViewById(R.id.checkBoxBacon);
            checkBoxQueijo = findViewById(R.id.checkBoxQueijo);
            checkBoxOnionRings = findViewById(R.id.checkBoxOnionRings);
            
            buttonMais = findViewById(R.id.buttonMais);
            buttonMenos = findViewById(R.id.buttonMenos);
            buttonEnviar = findViewById(R.id.buttonEnviar);
            
            // Configurar imagem da logo (simulada - em projeto real, viria de recursos)
            // imageViewLogo.setImageResource(R.drawable.logo_hamburgueria);
            
        } catch (NullPointerException e) {
            Toast.makeText(this, "Erro ao carregar componentes: " + e.getMessage(), 
                          Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Método para configurar os listeners dos botões
     */
    private void setupListeners() {
        // Listener do botão somar
        buttonMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                somar();
            }
        });
        
        // Listener do botão subtrair
        buttonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtrair();
            }
        });
        
        // Listener do botão enviar
        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarPedido();
            }
        });
        
        // Listeners para checkboxes (atualizar preço em tempo real)
        View.OnClickListener checkListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarPreco();
            }
        };
        
        checkBoxBacon.setOnClickListener(checkListener);
        checkBoxQueijo.setOnClickListener(checkListener);
        checkBoxOnionRings.setOnClickListener(checkListener);
    }
    
    /**
     * Método para incrementar a quantidade
     */
    private void somar() {
        quantidade++;
        textViewQuantidade.setText(String.valueOf(quantidade));
        atualizarPreco();
    }
    
    /**
     * Método para decrementar a quantidade (com validação)
     */
    private void subtrair() {
        if (quantidade > 0) {
            quantidade--;
            textViewQuantidade.setText(String.valueOf(quantidade));
            atualizarPreco();
        } else {
            Toast.makeText(this, "❌ Quantidade não pode ser negativa!", 
                          Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Método para calcular o preço total do pedido
     * @return int preço total calculado
     */
    private int calcularPrecoTotal() {
        int precoAdicionais = 0;
        
        if (checkBoxBacon.isChecked()) {
            precoAdicionais += PRECO_BACON;
        }
        if (checkBoxQueijo.isChecked()) {
            precoAdicionais += PRECO_QUEIJO;
        }
        if (checkBoxOnionRings.isChecked()) {
            precoAdicionais += PRECO_ONION;
        }
        
        return (PRECO_BASE + precoAdicionais) * quantidade;
    }
    
    /**
     * Método para atualizar a exibição do preço
     */
    private void atualizarPreco() {
        int precoTotal = calcularPrecoTotal();
        textViewPreco.setText(String.format("R$ %d,00", precoTotal));
    }
    
    /**
     * Método principal para processar e enviar o pedido
     */
    private void enviarPedido() {
        // Validação de quantidade
        if (quantidade == 0) {
            Toast.makeText(this, "⚠️ Selecione uma quantidade válida!", 
                          Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Coleta dos dados
        String nome = editTextNome.getText().toString().trim();
        if (nome.isEmpty()) {
            nome = "Cliente";
            Toast.makeText(this, "👤 Nome não informado. Usando 'Cliente'", 
                          Toast.LENGTH_SHORT).show();
        }
        
        // Montagem do resumo
        String resumo = montarResumoPedido(nome);
        textViewResumo.setText(resumo);
        
        // Envio por e-mail via Intent
        enviarPorEmail(nome, resumo);
        
        // Feedback visual
        Toast.makeText(this, "✅ Pedido processado com sucesso!", 
                      Toast.LENGTH_LONG).show();
    }
    
    /**
     * Método para montar o resumo formatado do pedido
     * @param nome Nome do cliente
     * @return String com o resumo formatado
     */
    private String montarResumoPedido(String nome) {
        StringBuilder resumo = new StringBuilder();
        
        resumo.append("🍔 HAMBURGUERIAZ - PEDIDO 🍔\n\n");
        resumo.append("Cliente: ").append(nome).append("\n");
        resumo.append("Quantidade: ").append(quantidade).append("x\n\n");
        
        resumo.append("ADICIONAIS:\n");
        resumo.append("• Bacon: ").append(checkBoxBacon.isChecked() ? "✅ Sim" : "❌ Não").append("\n");
        resumo.append("• Queijo: ").append(checkBoxQueijo.isChecked() ? "✅ Sim" : "❌ Não").append("\n");
        resumo.append("• Onion Rings: ").append(checkBoxOnionRings.isChecked() ? "✅ Sim" : "❌ Não").append("\n\n");
        
        resumo.append("💰 TOTAL: R$ ").append(calcularPrecoTotal()).append(",00 💰\n");
        resumo.append("📱 Pedido realizado via App HamburgueriaZ\n");
        resumo.append("\n🙏 Obrigado pela preferência!");
        
        return resumo.toString();
    }
    
    /**
     * Método para enviar o pedido por e-mail usando Intent
     * @param nome Nome do cliente
     * @param resumo Resumo do pedido
     */
    private void enviarPorEmail(String nome, String resumo) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "📱 Pedido HamburgueriaZ - " + nome);
            intent.putExtra(Intent.EXTRA_TEXT, resumo);
            
            // Verifica se existe algum app de e-mail instalado
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, "Enviar pedido por e-mail"));
            } else {
                Toast.makeText(this, "📧 Nenhum aplicativo de e-mail encontrado!", 
                              Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao enviar e-mail: " + e.getMessage(), 
                          Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Método para limpar o formulário (útil para novos pedidos)
     */
    private void limparFormulario() {
        editTextNome.setText("");
        checkBoxBacon.setChecked(false);
        checkBoxQueijo.setChecked(false);
        checkBoxOnionRings.setChecked(false);
        quantidade = 0;
        textViewQuantidade.setText("0");
        textViewResumo.setText("Seu pedido aparecerá aqui...");
        atualizarPreco();
    }
    
    /**
     * Sobrescrevendo onSaveInstanceState para preservar estado durante rotação
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("quantidade", quantidade);
        outState.putString("nome", editTextNome.getText().toString());
        outState.putBoolean("bacon", checkBoxBacon.isChecked());
        outState.putBoolean("queijo", checkBoxQueijo.isChecked());
        outState.putBoolean("onion", checkBoxOnionRings.isChecked());
        outState.putString("resumo", textViewResumo.getText().toString());
    }
    
    /**
     * Restaurando estado após rotação
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        quantidade = savedInstanceState.getInt("quantidade");
        editTextNome.setText(savedInstanceState.getString("nome"));
        checkBoxBacon.setChecked(savedInstanceState.getBoolean("bacon"));
        checkBoxQueijo.setChecked(savedInstanceState.getBoolean("queijo"));
        checkBoxOnionRings.setChecked(savedInstanceState.getBoolean("onion"));
        textViewResumo.setText(savedInstanceState.getString("resumo"));
        textViewQuantidade.setText(String.valueOf(quantidade));
        atualizarPreco();
    }
}