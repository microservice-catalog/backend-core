-- Обновление внешних ключей в таблице email_confirmation
alter table email_confirmation
    drop constraint fk_email_confirmation_account_id,
    add constraint fk_email_confirmation_account_id
        foreign key (account_id) references account
            on delete cascade;

-- Обновление внешних ключей в таблице project_info
alter table project_info
    drop constraint fk_project_info_author_account_id,
    add constraint fk_project_info_author_account_id
        foreign key (author_account_id) references account
            on delete cascade;

-- Обновление внешних ключей в таблице project_tag
alter table project_tag
    drop constraint fk_project_tag_project,
    add constraint fk_project_tag_project
        foreign key (project_id) references project_info
            on delete cascade;

alter table project_tag
    drop constraint fk_project_tag_tag,
    add constraint fk_project_tag_tag
        foreign key (tag_id) references tag
            on delete cascade;

-- Обновление внешних ключей в таблице project_user_favourite
alter table project_user_favourite
    drop constraint fk_project_user_favourite_project,
    add constraint fk_project_user_favourite_project
        foreign key (project_id) references project_info
            on delete cascade;

alter table project_user_favourite
    drop constraint fk_project_user_favourite_user,
    add constraint fk_project_user_favourite_user
        foreign key (user_id) references account
            on delete cascade;

-- Обновление внешних ключей в таблице project_user_pull
alter table project_user_pull
    drop constraint fk_project_user_pull_project,
    add constraint fk_project_user_pull_project
        foreign key (project_id) references project_info
            on delete cascade;

alter table project_user_pull
    drop constraint fk_project_user_pull_user,
    add constraint fk_project_user_pull_user
        foreign key (user_id) references account
            on delete cascade;

-- Обновление внешних ключей в таблице project_user_watch
alter table project_user_watch
    drop constraint fk_project_user_watch_project,
    add constraint fk_project_user_watch_project
        foreign key (project_id) references project_info
            on delete cascade;

alter table project_user_watch
    drop constraint fk_project_user_watch_user,
    add constraint fk_project_user_watch_user
        foreign key (user_id) references account
            on delete cascade;

-- Обновление внешних ключей в таблице project_version
alter table project_version
    drop constraint fk_project_info_versions,
    add constraint fk_project_info_versions
        foreign key (project_id) references project_info
            on delete set null;

-- Обновление внешних ключей в таблице project_env_param
alter table project_env_param
    drop constraint fk_project_env_param_project_version,
    add constraint fk_project_env_param_project_version
        foreign key (project_version_id) references project_version
            on delete cascade;

-- Обновление внешнего ключа для default_project_version в таблице project_info
alter table project_info
    drop constraint fk_project_info_default_project_version,
    add constraint fk_project_info_default_project_version
        foreign key (default_project_version) references project_version
            on delete cascade;
