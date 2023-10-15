package br.com.francisco.TodoList.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.francisco.TodoList.users.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class filterTaskAuth extends OncePerRequestFilter{
    @Autowired
    private UserRepository userRepository;

    

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

            var route = request.getServletPath();
                //criando rota
            if(route.startsWith("/tasks/")){


                // pegar a autenticação (usuário e senha)
                var authorization = request.getHeader("Authorization");
                var authEncode = authorization.substring("Basic".length()).trim();
                System.out.println("Authorization");
                System.out.println(authEncode);

                byte[] authDecode = Base64.getDecoder().decode(authEncode);
                var authString = new String(authDecode);
                String[] credentials = authString.split(":");
                String username = credentials[0];
                String password = credentials[1];
                // validar usuário
                var user = this.userRepository.findByUsername(username);
                if(user == null){
                    response.sendError(401);
                }else{
                    //validar senha
                    var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                    if(passwordVerify.verified){
                        //* neste espaço vou colocar uma linha de código para ajudar na funcionalidade pegar o idusers do cliente através da autentificação
                        request.setAttribute("idUser", user.getId());
                        //*
                        
                        filterChain.doFilter(request, response);
                    }else{
                        response.sendError(401);
                    }
                }
                
            }else{
                filterChain.doFilter(request, response);
            }    

                
    }

   
    
}
