package de.frosner.broccoli.models

import java.util.regex.Pattern

import play.api.libs.json._
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax

import scala.collection.mutable.ArrayBuffer

case class Template(id: String, template: String) {

  val parameters: Set[String] = {
    val matcher = Template.TemplatePattern.matcher(template)
    var variables = ArrayBuffer[String]()
    while (matcher.find()) {
      variables += matcher.group(1)
    }
    variables.toSet
  }

}

object Template {

  val TemplatePattern = Pattern.compile("\\$\\{([A-Za-z][A-Za-z0-9\\_]*)\\}")

  // TODO read templates from existing template files + check that all prefixes match with the defined one (e.g. ds)
  var templates = Seq(Template("1", "Hallo ${name}"), Template("2", "Hallo ${name}, how is ${object}"))

  implicit val templateWrites: Writes[Template] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "template").write[String] and
      (JsPath \ "parameters").write[Set[String]]
    )((template: Template) => (template.id, template.template, template.parameters))

  implicit val templateReads: Reads[Template] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "template").read[String]
    )(Template.apply _)

}