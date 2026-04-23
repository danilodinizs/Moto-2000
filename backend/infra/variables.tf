variable "aws_region" {
  description = "Região onde os recursos serão criados"
  type        = string
  default     = "us-east-1" # N. Virginia (Geralmente a mais barata)
}

variable "project_name" {
  description = "Nome do projeto para identificação nos recursos"
  type        = string
  default     = "Moto2000"
}

variable "instance_type" {
  description = "Tipo da instância para o servidor de aplicação"
  type        = string
  default     = "t3.micro" # Ótimo para o Free Tier da AWS
}

variable "environment" {
  description = "Ambiente do projeto (dev, staging, prod)"
  type        = string
  default     = "dev"
}

# 5. Chave SSH (Opcional, mas recomendado para acessar o servidor)
variable "key_name" {
  description = "Nome da chave SSH cadastrada na AWS para acesso à instância"
  type        = string
  default     = "minha-chave-wsl"
}