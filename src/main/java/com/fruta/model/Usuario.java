package com.fruta.model;

/**
 * Alias para Cliente - mantém compatibilidade com código existente
 * @deprecated Use Cliente em vez disso
 */
@Deprecated(forRemoval = false)
public class Usuario extends Cliente {
    public Usuario() {
        super();
    }
}
