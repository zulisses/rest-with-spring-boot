CREATE TABLE public.user_permission (
    id_user bigint NOT NULL,
    id_permission bigint NOT NULL
);

ALTER TABLE ONLY public.user_permission
    ADD CONSTRAINT user_permission_pkey PRIMARY KEY (id_user, id_permission);

ALTER TABLE ONLY public.user_permission
    ADD CONSTRAINT fk_user_permission FOREIGN KEY (id_user) REFERENCES public.users (id);

ALTER TABLE ONLY public.user_permission
    ADD CONSTRAINT fk_user_permission_permission FOREIGN KEY (id_permission) REFERENCES public.permission (id);

CREATE INDEX fk_user_permission_permission ON public.user_permission (id_permission);
