package com.example.procdpchallenger.adapter.inbound.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    /**
     * HTTPリクエストごとに実行されるフィルタ処理。
     * リクエストヘッダーからJWTトークンを取得し、トークンが有効であれば
     * 認証情報を作成してSpring SecurityのSecurityContextに設定する。
     *
     * @param request     HTTPリクエスト
     * @param response    HTTPレスポンス
     * @param filterChain フィルタチェーン
     * @throws ServletException フィルタ処理中にエラーが発生した場合
     * @throws IOException      入出力処理中にエラーが発生した場合
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Authorizationヘッダーからトークンを取得
        String token = resolveToken(request);

        if (token != null && tokenProvider.validateToken(token)) {
            // トークンからユーザー情報を取得
            String username = tokenProvider.extractUserId(token);

            // 認証情報を作成し、SecurityContextに設定
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username, null, null // 必要に応じて権限情報を設定
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 次のフィルタに処理を委譲
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer "を取り除く
        }
        return null;
    }
}
