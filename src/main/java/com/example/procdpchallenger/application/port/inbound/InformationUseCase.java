package com.example.procdpchallenger.application.port.inbound;

import java.util.List;

public interface InformationUseCase {
    String getType(); 
    /*
     * 戻り値を抽象化するために、List<?>を使用する
     * 戻り値の型は、実装クラスで決定される
     * 
     * <?>は、特定の型を指定せずに任意の型を受け入れることを示すが、必ず何らかのジェネリック型と組み合わせて使用される
     * そのため、List<?>を使用する
     */
    List<?> execute(); 
}
