package de.frosner.broccoli.controllers

import javax.inject.Inject

import de.frosner.broccoli.models.Template
import Template.templateWrites
import de.frosner.broccoli.services.TemplateService

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}


class TemplateController @Inject() (templateService: TemplateService) extends Controller {

  private val templates = templateService.templates

  def list = Action {
    Ok(Json.toJson(templateService.templates))
  }

  def show(id: String) = Action {
    templates.find(_.id == id).map(template => Ok(Json.toJson(template))).getOrElse(NotFound)
  }

}
