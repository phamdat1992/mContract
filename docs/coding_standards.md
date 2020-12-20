# MContract - Conding standards

These are the standards being followed in this project that helps writing cleaner code
and maintain consistency.

## Things to avoid
1. Do NOT keep secret key and password in commit.
2. Do NOT commit local development files. Ignore them instead. (e.g. `/.idea`)

## Naming Conventions

1. API endpoints URL
Kebab case (`kebab-case`)
Avoid trailing forward slash (`/`)
```
www.example.com/sign-document
```
2. Formdata keys
Snake case (`snake_case`)
```
{
    "signature_value": "XXX"
}
```

