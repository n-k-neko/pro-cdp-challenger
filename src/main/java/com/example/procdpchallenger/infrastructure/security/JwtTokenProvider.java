package com.example.procdpchallenger.infrastructure.security;

import com.example.procdpchallenger.adapter.inbound.security.JwtToken;
import com.example.procdpchallenger.adapter.inbound.security.TokenProvider;
import com.example.procdpchallenger.domain.user.valueobject.UserId;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

import java.util.Date;

/**
 * JSON Web Token（JWT）の管理を担当するユーティリティクラス。
 * トークンの生成、検証、およびトークンからの情報抽出を行う。
 * JWTに関連する技術的な詳細をカプセル化する。
 */
/*
    TODO:トークンに専用オブジェクトを導入するかどうか検討する
    一般的な用法であればトークンはString型でそのまま扱うようだが、
    個人的には型安全性や拡張性を想定すると専用のオブジェクトを導入することも検討したい。
 */
@Component
public class JwtTokenProvider implements TokenProvider {
    // トークン生成および検証に使用する秘密鍵
    private final String secretKey;

    // コンストラクタ
    public JwtTokenProvider() {
        // 環境変数から秘密鍵を取得
        // TODO:将来的にはAWS Secret Managerなどのシークレット管理サービスを使用する
        this.secretKey = System.getenv("JWT_SECRET_KEY");
        if (this.secretKey == null || this.secretKey.isEmpty()) {
            throw new IllegalStateException("JWT_SECRET_KEY is not set!");
        }
    }

    /**
     * 指定されたユーザー名に基づいてJWTを生成する。
     *
     * @param userId JWTを生成する対象のユーザーID
     * @return 生成されたJWT（文字列）
     */
    @Override
    public String generateToken(UserId userId) {
        Date now = new Date();
        Date validity = JwtToken.calculateExpirationDate(now);

        return Jwts.builder()
                .setSubject(userId.value())
                .setIssuer(JwtToken.ISSUER)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey) // 署名アルゴリズムと秘密鍵を設定
                .compact();
    }

    /**
     * 指定されたJWTが有効かどうかを検証する。
     *
     * @param token 検証対象のJWT
     * @return トークンが有効であればtrue、無効であればfalse
     */
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey) // トークンを検証するための秘密鍵を設定
                    .build()
                    .parseClaimsJws(token); // トークンを解析
            return true; // トークンが有効である場合
        } catch (ExpiredJwtException e) {
            System.err.println("Token has expired: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("Invalid token format: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("Unsupported token: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Empty or null token: " + e.getMessage());
        }
        return false; // トークンが無効である場合
    }

    /**
     * 指定されたJWTからユーザーIDを抽出する。
     *
     * @param token 情報を抽出するJWT
     * @return トークン内に含まれるユーザーID
     */
    @Override
    public String extractUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey) // 秘密鍵を設定
                .parseClaimsJws(token) // トークンを解析
                .getBody(); // クレーム情報を取得

        return claims.getSubject(); // ユーザーIDを返す（setSubjectで設定した値）
    }
}
