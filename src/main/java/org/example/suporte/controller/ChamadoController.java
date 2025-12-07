package org.example.suporte.controller;

import org.example.suporte.dto.ChamadoDTO;
import org.example.suporte.exception.ErroExternoException;
import org.example.suporte.exception.ValidacaoException;
import org.example.suporte.model.Chamado;
import org.example.suporte.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService service;

    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("chamados", service.listarTodos());
        return "chamados-list";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("chamado", ChamadoDTO.novoPadrao());
        return "chamados-form";
    }

    @PostMapping
    public String criar(@ModelAttribute("chamado") @Valid ChamadoDTO dto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "chamados-form";
        }
        try {
            service.criarChamado(dto);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Chamado criado com sucesso!");
        } catch (ValidacaoException e) {
            bindingResult.reject("erro.validacao", e.getMessage());
            return "chamados-form";
        } catch (ErroExternoException e) {
            redirectAttributes.addFlashAttribute("mensagemAviso", "Chamado criado, mas houve falha ao notificar. Tente novamente mais tarde.");
        }
        return "redirect:/chamados";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        Chamado chamado = service.buscarPorId(id);
        model.addAttribute("chamado", ChamadoDTO.fromEntity(chamado));
        return "chamados-form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @ModelAttribute("chamado") @Valid ChamadoDTO dto,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "chamados-form";
        }
        service.atualizar(id, dto);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Chamado atualizado com sucesso!");
        return "redirect:/chamados";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        service.excluir(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Chamado excluído!");
        return "redirect:/chamados";
    }
}
