package cn.qd.service;

import cn.qd.entity.Template;

import java.util.List;

public interface TemplateService {

    public List<Template> findAll();

    public void add(Template template);

    public Template findById(Long id);
}
