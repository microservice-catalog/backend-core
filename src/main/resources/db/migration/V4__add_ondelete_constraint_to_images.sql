-- Обновление внешнего ключа для default_project_version в таблице project_info
alter table images
    drop constraint if exists fk_image_entity_account_id,
    add constraint fk_image_entity_account_id
        foreign key (account_id) references account
            on delete set null;

