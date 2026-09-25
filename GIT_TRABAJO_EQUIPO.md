# Guía Git - StockAndes

Flujo mínimo exigido por el examen:

```bash
git checkout develop
git pull origin develop
git checkout -b feature/despacho-apellido

# commits pequeños

git add .
git commit -m "feat(despacho): registrar cabecera y detalle en una transacción"
git push -u origin feature/despacho-apellido
```

Al integrar:

```bash
git checkout develop
git merge --no-ff feature/despacho-apellido
git push origin develop
```

Cierre:

```bash
git checkout main
git merge --no-ff develop
git tag -a v1.0-unidad1 -m "Producto Unidad 1 - StockAndes"
git push origin main --tags
```

Durante la Parte II usa `sc-<letra>-<apellido>` y registra al menos tres commits separados en el tiempo.
