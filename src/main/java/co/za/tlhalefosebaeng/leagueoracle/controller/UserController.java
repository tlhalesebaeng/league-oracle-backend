package co.za.tlhalefosebaeng.leagueoracle.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.endpoint.prefix}/auth")
public class UserController {
}
